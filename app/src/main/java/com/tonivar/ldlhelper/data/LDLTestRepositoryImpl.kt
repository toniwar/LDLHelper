package com.tonivar.ldlhelper.data

import com.tonivar.ldlhelper.data.db.DBEmulator
import com.tonivar.ldlhelper.domain.LDLTestRepository
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question
import java.io.File

class LDLTestRepositoryImpl: LDLTestRepository {

    override fun getTest(file: File) {
        val map = TestProvider().getExcelFileFromStorage(file)
        DBEmulator.fillTestList(map)
    }

    override fun getChapters(): List<Chapter> {
        val list = arrayListOf<Chapter>()
        for (entry in DBEmulator.getTestMap()) {
            list.add(Mapper.testChapterToChapter(entry.key))
        }
        return list.toList()
    }

    override fun getQuestions(chapter: Chapter, range: IntRange): List<Question> {
        val list = arrayListOf<Question>()
        for (entry in DBEmulator.getTestMap()) {
            if (chapter.id == entry.key.id) {
                for (question in entry.value) {
                    list.add(Mapper.testQuestionToQuestion(question))
                }
                break
            }
        }
        return list.subList(range.first, range.last).toList()
    }

    override fun findQuestion(text: String): Question {
        TODO("Not yet implemented")
    }
}