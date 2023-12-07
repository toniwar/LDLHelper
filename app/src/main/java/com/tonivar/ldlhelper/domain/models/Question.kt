package com.tonivar.ldlhelper.domain.models

data class Question(
    val id: Int,
    val question: String,
    val answers: List<Answer>
): UserData