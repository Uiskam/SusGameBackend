package edu.agh.susgame.back.domain.models

import edu.agh.susgame.back.domain.build.GameInitializer
import edu.agh.susgame.back.domain.models.quiz.QuizManager
import edu.agh.susgame.back.domain.net.BFS
import edu.agh.susgame.back.domain.net.NetGraph
import edu.agh.susgame.back.domain.net.Player
import edu.agh.susgame.back.domain.net.node.Host
import edu.agh.susgame.back.services.rest.RestParser
import edu.agh.susgame.back.services.socket.GamesWebSocketConnection
import edu.agh.susgame.config.*
import edu.agh.susgame.dto.common.ColorDTO
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.socket.ClientSocketMessage
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Game(
    val name: String,
    val id: Int,
    val maxNumberOfPlayers: Int,
    val gamePin: String? = null,
) {

    @Volatile
    private var gameStatus: GameStatus = GameStatus.WAITING

    private val playerMap: ConcurrentHashMap<GamesWebSocketConnection, Player> = ConcurrentHashMap()
    private lateinit var playersInGame: List<Player>
    private var nextPlayerIdx: AtomicInteger = AtomicInteger(0)

    lateinit var netGraph: NetGraph
    private var gameLength: Int = GAME_TIME_DEFAULT
    var gameGoal: Int = GAME_DEFAULT_PACKETS_DELIVERED_GOAL
    private lateinit var bfs: BFS

    private var startTime: Long = -1

    private val quizManager = QuizManager()

    fun getTimeLeftInSeconds(): Int {
        return ((gameLength - (System.currentTimeMillis() - startTime)) / 1000).toInt()
    }

    suspend fun addPlayer(connection: GamesWebSocketConnection, playerName: String): Player {
        val playerIndex = getNextPlayerIdx()
        connection.sendServerSocketMessage(ServerSocketMessage.IdConfig(playerIndex))
        val newPlayer = Player(index = playerIndex, name = playerName)

        if (playerMap.values.any { it.name == newPlayer.name }) {
            throw IllegalArgumentException("Player with name ${newPlayer.name} already exists")
        }

        playerMap[connection] = newPlayer
        handlePlayerJoiningRequest(connection, newPlayer)
        return newPlayer
    }

    fun reconnectPlayer(connection: GamesWebSocketConnection, playerId: Int): Player {
        val player = playersInGame.find { it.index == playerId }
            ?: throw IllegalArgumentException("Player with id $playerId not found")
        if (playerMap.values.find { it == player } != null) {
            throw IllegalArgumentException("Player with id $playerId is in game")
        }
        playerMap[connection] = player
        return player
    }

    fun removePlayer(connection: GamesWebSocketConnection) {
        playerMap.remove(connection)
    }

    fun getDataToReturn(): Lobby {
        return Lobby(
            id = LobbyId(id),
            name = name,
            maxNumOfPlayers = maxNumberOfPlayers,
            // TODO GAME-74 Remove this hardcoded value
            gameTime = 10,
            playersWaiting = playerMap.values.map { it.toREST() },
        )
    }

    fun getGameStatus(): GameStatus = gameStatus

    fun getPlayers(): Map<GamesWebSocketConnection, Player> = playerMap.toMap()

    private fun getNextPlayerIdx(): Int = nextPlayerIdx.getAndIncrement()

    private fun addMoneyPerIterationForAllPlayers() {
        playerMap.values.forEach { it.addMoneyPerIteration() }
    }

    private fun areAllPlayersReady() = playerMap.values.all { it.isReady }

    /**
     * Starts the game by generating the graph, setting the start time and changing the game status to running
     */
    private fun startGame(graph: NetGraph? = null) {
        val (parsedGraph, gameLength, gameGoal) = GameInitializer.getGameParams(playerMap.values.toList())
        this.gameGoal = gameGoal
        this.gameLength = gameLength
        playersInGame = playerMap.values.toList()
        netGraph = graph ?: parsedGraph
        bfs = BFS(net = netGraph, root = netGraph.getServer())
        gameStatus = GameStatus.RUNNING
        startTime = System.currentTimeMillis()
    }

    /**
     * Ends the game if certain conditions are met.
     *
     * The game will end if:
     * - The total packets delivered to the server(s) are greater than or equal to the game goal.
     * - The current time exceeds the game length since the start time.
     *
     * The game status will be updated to either FINISHED_WON or FINISHED_LOST based on the conditions.
     */
    private fun endGameIfPossible() {
        gameStatus = when {
            netGraph.getTotalPacketsDelivered() >= gameGoal -> {
                GameStatus.FINISHED_WON
            }

            System.currentTimeMillis() - startTime > gameLength -> {
                GameStatus.FINISHED_LOST
            }

            else -> {
                gameStatus
            }
        }
    }

    /*
     * ##################################################
     * HANDLERS
     * ##################################################
     *
     * Lobby handlers
     */

    private suspend fun handlePlayerJoiningRequest(thisConnection: GamesWebSocketConnection, thisPlayer: Player) {
        playerMap
            .filter { it.key != thisConnection }
            .forEach { (connection, _) ->
                connection.sendServerSocketMessage(
                    ServerSocketMessage.PlayerJoining(playerId = thisPlayer.index, playerName = thisPlayer.name)
                )
            }
    }

    suspend fun handlePlayerChangeReadinessRequest(
        thisConnection: GamesWebSocketConnection,
        thisPlayer: Player,
        receivedMessage: ClientSocketMessage.PlayerChangeReadiness
    ) {

        if (gameStatus != GameStatus.WAITING) {
            sendErrorMessage("Invalid game status on server")
            return
        }

        val readinessState: Boolean = receivedMessage.state
        thisPlayer.setReadinessState(readinessState)
        playerMap
            .filter { it.key != thisConnection }
            .forEach { (connection, _) ->
                connection.sendServerSocketMessage(
                    ServerSocketMessage.PlayerChangeReadiness(playerId = thisPlayer.index, state = readinessState)
                )
            }
    }

    suspend fun handlePlayerChangeColor(
        thisConnection: GamesWebSocketConnection,
        thisPlayer: Player,
        receivedMessage: ClientSocketMessage.PlayerChangeColor
    ) {

        if (gameStatus != GameStatus.WAITING) {
            sendErrorMessage("Invalid game status on server")
            return
        }

        val color: ULong = receivedMessage.color.decimalRgbaValue.toULong()
        thisPlayer.setColor(color)
        playerMap
            .filter { it.key != thisConnection }
            .forEach { (connection, _) ->
                connection.sendServerSocketMessage(
                    ServerSocketMessage.PlayerChangeColor(
                        playerId = thisPlayer.index,
                        color = ColorDTO(color.toString())
                    )
                )
            }
    }

    suspend fun handlePlayerLeavingRequest(thisConnection: GamesWebSocketConnection, thisPlayer: Player) {
        playerMap
            .filter { it.key != thisConnection }
            .forEach { (connection, _) ->
                connection.sendServerSocketMessage(
                    ServerSocketMessage.PlayerLeaving(playerId = thisPlayer.index)
                )
            }
    }

    /*
     * Game handlers
     */

    suspend fun handleChatMessage(
        thisConnection: GamesWebSocketConnection, thisPlayer: Player, receivedMessage: ClientSocketMessage.ChatMessage
    ) {
        playerMap.filter { it.key != thisConnection }.forEach { (connection, _) ->
            connection.sendServerSocketMessage(
                ServerSocketMessage.ChatMessage(
                    authorNickname = thisPlayer.name,
                    message = receivedMessage.message,
                )
            )
        }
    }

    suspend fun handleGameState(receivedGameStatus: ClientSocketMessage.GameState, webSocket: WebSocketSession) {
        if (receivedGameStatus.gameStatus != GameStatus.RUNNING) {
            sendErrorMessage("Invalid game status sent by client")
            return
        }

        if (gameStatus != GameStatus.WAITING) {
            sendErrorMessage("Invalid game status on server")
            return
        }

        if (areAllPlayersReady()) {
            gameStatus = GameStatus.RUNNING
            try {
                startGame()
            } catch (e: IllegalArgumentException) {
                sendErrorMessage(e.message ?: "Unknown error")
                return
            }
            notifyAllAboutGameStart()

            // GAME IS RUNNING
            broadcastStateThread(webSocket)
            runEngineIterationThread(webSocket)
            quizManager.init(playerMap)
        } else {
            sendErrorMessage("Not all players are ready")
        }


    }

    suspend fun handleHostRoute(
        receivedMessage: ClientSocketMessage.HostRouteDTO
    ) {
        val host = safeRetrieveHost(receivedMessage.id)
        val route = receivedMessage.packetPath.flatMap { nodeId ->
            when (val node = netGraph.getNodeById(nodeId)) {
                null -> emptyList()
                else -> listOf(node)
            }
        }
        host.setRoute(route)
    }

    suspend fun handleHostFlow(
        receivedMessage: ClientSocketMessage.HostFlowDTO
    ) {
        val host = safeRetrieveHost(receivedMessage.id)
        host.setMaxPacketsPerTick(receivedMessage.packetsSentPerTick)
    }

    suspend fun handleUpgradeDTO(
        thisConnection: GamesWebSocketConnection, receivedMessage: ClientSocketMessage.UpgradeDTO, thisPlayer: Player
    ) {
        if (gameStatus != GameStatus.RUNNING) {
            sendErrorMessage("Invalid game status on server: Game is not running")
            return
        }
        try {
            val deviceIdToUpgrade = receivedMessage.deviceId

            val edge = netGraph.getEdgeById(deviceIdToUpgrade)
            val router = netGraph.getRouter(deviceIdToUpgrade)

            if (edge != null) {
                edge.upgradeWeight(thisPlayer)
            } else if (router != null) {
                router.upgradeBuffer(thisPlayer)
            } else {
                sendErrorMessage("There is neither an edge not a host with id of $deviceIdToUpgrade.")
            }
        } catch (e: IllegalStateException) {
            thisConnection.sendServerSocketMessage(
                ServerSocketMessage.ServerError(e.message ?: "Unknown error")
            )
        }
    }

    suspend fun handleFixRouterDTO( thisConnection: GamesWebSocketConnection, receivedMessage: ClientSocketMessage.FixRouterDTO) {
        if (gameStatus != GameStatus.RUNNING) {
            sendErrorMessage("Invalid game status on server: Game is not running")
            return
        }

        try {
            val deviceIdToUpgrade = receivedMessage.deviceId
            val router = netGraph.getRouter(deviceIdToUpgrade)
            router?.fixBuffer() ?: sendErrorMessage("There is no host with id of $deviceIdToUpgrade")
        } catch (e: IllegalStateException) {
            thisConnection.sendServerSocketMessage(
                ServerSocketMessage.ServerError(e.message ?: "Unknown error")
            )
        }
    }

    suspend fun handleQuizAnswerDTO(
        thisConnection: GamesWebSocketConnection,
        receivedMessage: ClientSocketMessage.QuizAnswerDTO, thisPlayer: Player
    ) {
        quizManager.answerQuestion(thisPlayer, thisConnection, receivedMessage.answer)
    }

    /*
     * Other messages
     */
    private suspend fun sendErrorMessage(errorMessage: String) {
        playerMap.forEach { (connection, _) ->
            connection.sendServerSocketMessage(
                ServerSocketMessage.ServerError(errorMessage = errorMessage)
            )
        }
    }

    /*
     * ##################################################
     * THREADS
     * ##################################################
     */

    private fun broadcastStateThread(webSocket: WebSocketSession) {
        webSocket.launch {
            while (gameStatus == GameStatus.RUNNING) {
                endGameIfPossible()
                playerMap.forEach { (connection, _) ->
                    connection.sendServerSocketMessage(
                        RestParser.gameToGameState(this@Game)
                    )
                }
                delay(CLIENT_REFRESH_FREQUENCY)  // delay should be used from kotlinx.coroutines
            }
        }
    }

    private fun runEngineIterationThread(webSocket: WebSocketSession) {
        webSocket.launch {
            while (gameStatus == GameStatus.RUNNING) {
                delay(BFS_FREQUENCY)
                addMoneyPerIterationForAllPlayers()
                bfs.run()
            }
        }
    }

    /*
     * ##################################################
     * AUXILIARY FUNCTIONS
     * ##################################################
     */

    private suspend fun notifyAllAboutGameStart() {
        playerMap
            .forEach { (connection, _) ->
                connection.sendServerSocketMessage(
                    ServerSocketMessage.GameStarted(0)
                )
            }
    }

    private suspend fun safeRetrieveHost(id: Int): Host {
        if (gameStatus != GameStatus.RUNNING) {
            val errorMessage = "Invalid game status on server: Game is not running"
            sendErrorMessage(errorMessage)
            throw IllegalStateException(errorMessage)
        }
        val host = netGraph.getHost(id)

        if (host == null) {
            val errorMessage = "There is no host with id of $id"
            sendErrorMessage(errorMessage)
            throw IllegalStateException(errorMessage)
        }

        return host
    }
}
