package com.tonivar.ldlhelper.domain.models

data class Chapter(
    val id: Int,
    val questions: List<Question>,
    val title: String,
) : UserData
