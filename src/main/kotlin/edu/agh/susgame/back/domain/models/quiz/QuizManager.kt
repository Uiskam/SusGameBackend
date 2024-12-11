package edu.agh.susgame.back.domain.models.quiz

import edu.agh.susgame.back.domain.net.Player
import edu.agh.susgame.back.services.socket.GamesWebSocketConnection
import edu.agh.susgame.config.GAME_QUESTION_SENDING_INTERVAL
import edu.agh.susgame.dto.socket.ServerSocketMessage
import kotlinx.coroutines.delay
import java.util.concurrent.ConcurrentHashMap


data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int,
)

class QuizManager{
    private val playerQuizState = mutableMapOf<Player, Int?>()
    private val quizQuestions = QuizQuestions

    private fun getRandomQuestion(): Pair<Int, QuizQuestion> {
        val randomIndex = QuizQuestions.indices.random()
        return Pair(randomIndex, QuizQuestions[randomIndex])
    }

    suspend fun answerQuestion(player: Player, connection: GamesWebSocketConnection, answer: Int) {
        val questionId =
            playerQuizState[player] ?: throw IllegalStateException("Player $player has no question assigned")
        val correctAnswer = quizQuestions[questionId].correctAnswer
        if (answer == correctAnswer) {
            player.addMoneyForCorrectAnswer()
        }
        delay(GAME_QUESTION_SENDING_INTERVAL)
        assignNewQuestionForPlayer(player, connection)
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

    suspend fun init(playerMap: Map<GamesWebSocketConnection, Player>) {
        delay(GAME_QUESTION_SENDING_INTERVAL)
        playerMap.forEach { (connection, player) ->
            assignNewQuestionForPlayer(player, connection)
        }
    }

}