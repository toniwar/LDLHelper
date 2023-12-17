package com.tonivar.ldlhelper.domain.models

data class Question(
    val id: Int,
    val answers: List<Answer>,
    val question: String
): UserData