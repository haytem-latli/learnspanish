package com.example.learnspanish.models

data class Duel(
    val duelId: String = "",
    val playerOneId: String = "",
    val playerTwoId: String = "",
    val status: String = "",
    val winnerId: String? = null,
    val tasks: List<String> = emptyList(),
    val createdAt: Long = 0L
)