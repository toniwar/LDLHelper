package com.tonivar.ldlhelper.domain

import android.app.Activity
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question

interface LDLTestRepository {

    fun getTest(activity: Activity)

    fun getChapters(): List<Chapter>

    fun getQuestions(chapter: Chapter, range: IntRange) : List<Question>

    fun findQuestion(text: String): Question
}