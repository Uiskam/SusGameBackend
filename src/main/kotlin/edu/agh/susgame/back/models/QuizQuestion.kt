package edu.agh.susgame.back.models
import edu.agh.susgame.config.QuizQuestions

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int,
)