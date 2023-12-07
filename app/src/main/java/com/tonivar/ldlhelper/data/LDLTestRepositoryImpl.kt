package com.tonivar.ldlhelper.data

import android.app.Activity
import com.tonivar.ldlhelper.domain.LDLTestRepository
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question

class LDLTestRepositoryImpl: LDLTestRepository {

    override fun getTest(activity: Activity) {
        TestProvider().getExcelFileFromStorage(activity)
    }

    override fun getChapters(): List<Chapter> {
        TODO("Not yet implemented")
    }

    override fun getQuestions(chapter: Chapter, range: IntRange): List<Question> {
        TODO("Not yet implemented")
    }

    override fun findQuestion(text: String): Question {
        TODO("Not yet implemented")
    }
}