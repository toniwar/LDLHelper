package com.tonivar.ldlhelper.domain

import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question
import com.tonivar.ldlhelper.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface LDLTestRepository {

    suspend fun getTest(source: List<String>): Flow<Response>

    fun saveTestFileName(fileName: String)

    fun getTestFileName(): Flow<String>

    fun getChapters(): Flow<List<Chapter>>

    fun getQuestions(chapter: Chapter, range: IntRange) : List<Question>

    fun findQuestion(text: String): Question
}