package com.example.learnspanish.models

data class StoreItem(
    val itemId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val type: String = "",
    val imageUrl: String = ""
)