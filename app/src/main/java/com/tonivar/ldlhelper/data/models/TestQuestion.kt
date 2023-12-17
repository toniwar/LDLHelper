package com.tonivar.ldlhelper.data.models

data class TestQuestion(
    val id: Int,
    val answers: ArrayList<TestAnswer> = arrayListOf(),
    val question: String,
)
