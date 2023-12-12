package com.tonivar.ldlhelper.data.models

data class TestQuestion(
    val id: Int,
    val question: String,
    val answers: ArrayList<TestAnswer> = arrayListOf()
)
