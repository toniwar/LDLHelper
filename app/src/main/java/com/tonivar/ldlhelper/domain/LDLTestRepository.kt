package com.tonivar.ldlhelper.domain

import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question
import java.io.File

interface LDLTestRepository {

    fun getTest(file: File)

    fun getChapters(): List<Chapter>

    fun getQuestions(chapter: Chapter, range: IntRange) : List<Question>

    fun findQuestion(text: String): Question
}