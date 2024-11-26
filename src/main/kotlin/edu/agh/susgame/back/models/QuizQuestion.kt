package edu.agh.susgame.back.models

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int,
)