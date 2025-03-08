package com.example.learnspanish.models

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String="",
    val level: Int = 1,
    val xp: Int = 0,
    val coins: Int = 0,
    val purchasedItems: List<String> = emptyList(),
    val topicsProgress: Map<String, TopicProgress> = mapOf()
)

data class TopicProgress(
    val completedSubtopics: Int = 0,
    val totalSubtopics: Int = 0,
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val subtopics: Map<String, SubtopicProgress> = mapOf()
) {
    constructor() : this(0, 0, 0, 0, mapOf())
}

data class SubtopicProgress(
    val completedTasks: Int = 0,
    val totalTasks: Int = 0,
    val title: String = ""
) {
    constructor() : this(0, 0, "")
}
