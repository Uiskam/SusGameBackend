package edu.agh.susgame.back.domain.models.quiz

import edu.agh.susgame.back.domain.net.Player
import edu.agh.susgame.back.services.socket.GamesWebSocketConnection
import edu.agh.susgame.config.GAME_QUESTION_SENDING_INTERVAL
import edu.agh.susgame.dto.socket.ServerSocketMessage
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader


data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int,
)

class QuizManager {
    private val playerQuizState = mutableMapOf<Player, Int?>()
    private var quizQuestions = mutableListOf<QuizQuestion>()

    private fun getRandomQuestion(): Pair<Int, QuizQuestion> {
        val randomIndex = quizQuestions.indices.random()
        return Pair(randomIndex, quizQuestions[randomIndex])
    }

    fun answerQuestion(
        webSocket: WebSocketSession,
        player: Player,
        connection: GamesWebSocketConnection,
        answer: Int
    ) {
        webSocket.launch {
            val questionId =
                playerQuizState[player] ?: throw IllegalStateException("Player $player has no question assigned")
            val correctAnswer = quizQuestions[questionId].correctAnswer
            if (answer == correctAnswer) {
                player.addMoneyForCorrectAnswer()
            }
            delay(GAME_QUESTION_SENDING_INTERVAL)
            assignNewQuestionForPlayer(player, connection)
        }
    }

    private suspend fun assignNewQuestionForPlayer(player: Player, connection: GamesWebSocketConnection) {
        val (questionId, question) = getRandomQuestion()
        playerQuizState[player] = questionId
        
        connection.sendServerSocketMessage(
            ServerSocketMessage.QuizQuestionDTO(
                questionId = questionId,
                question = question.question,
                answers = question.answers,
                correctAnswer = question.correctAnswer,
            )
        )
    }

    fun init(webSocket: WebSocketSession, playerMap: Map<GamesWebSocketConnection, Player>) {
        val reader = BufferedReader(FileReader("game_files/pytania.csv"))
        val header = reader.readLine() // Pominięcie nagłówka
        val semicolonCount = header.count { it == ';' }
        println("header: $header, semicolonCount: $semicolonCount")
        val separator: String = if (semicolonCount > 2) {
            ";"
        } else {
            ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"
        }
        reader.lineSequence().forEachIndexed { index, line ->
            val parts = line.split(Regex(separator)).map { it.trim('"') }
            if (parts.size == 1 && parts[0].isBlank()) {
                return@forEachIndexed
            }
            if (parts.size == 6) {
                val question = parts[0]
                val answers = parts.subList(1, parts.size - 1)
                var correctAnswer = parts.last().toIntOrNull()
                    ?: throw IllegalArgumentException("Numer poprawnej odpowiedzi nie jest liczbą w wierszu: $index")
                if (correctAnswer < 1 || correctAnswer > 4) {
                    throw IllegalArgumentException("Numer poprawnej odpowiedzi jest poza zakresem 1-4 w wierszu: $index")
                }
                correctAnswer--
                quizQuestions.add(QuizQuestion(question, answers, correctAnswer))
            } else {
                throw IllegalArgumentException("Zła liczba wartości (${parts.size}) w wierszu: $line")
            }
        }
        reader.close()

        webSocket.launch {
            delay(GAME_QUESTION_SENDING_INTERVAL)
            playerMap.forEach { (connection, player) ->
                assignNewQuestionForPlayer(player, connection)
            }
        }
    }

}