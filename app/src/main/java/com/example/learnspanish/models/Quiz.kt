package com.example.learnspanish.models
data class Quiz(
    val title: String,
    val questions: List<Question>
)

data class Question(
    val questionText: String,
    val choices: List<String>,
    val correctAnswer: Int
)