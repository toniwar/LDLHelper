package com.tonivar.ldlhelper.data

import android.content.Context
import com.tonivar.ldlhelper.DOWNLOAD_ERROR
import com.tonivar.ldlhelper.DOWNLOAD_SUCCESS
import com.tonivar.ldlhelper.FILE_DOWNLOADED
import com.tonivar.ldlhelper.FILE_NOT_FOUND
import com.tonivar.ldlhelper.domain.LDLTestRepository
import com.tonivar.ldlhelper.domain.models.AppResponse
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question
import com.tonivar.ldlhelper.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LDLTestRepositoryImpl(val context: Context): LDLTestRepository {

    override suspend fun getTest(source: List<String>): Flow<Response> {
        return flow {
            val test = TestProvider().getExcelFileFromStorage(source)
            val result = LocalStorage.downLoadTest(test)
            LocalStorage.saveTestToSharedPrefs(context)
            LocalStorage.refreshData(context)

            val responseCode = if (!result) FILE_NOT_FOUND
            else FILE_DOWNLOADED
            val message = if (responseCode == FILE_NOT_FOUND) DOWNLOAD_ERROR else DOWNLOAD_SUCCESS
            val response = AppResponse(responseCode, message)
            emit(response)
        }
    }

    override fun saveTestFileName(fileName: String) {
        LocalStorage.saveTestFileName(fileName, context)
    }

    override fun getTestFileName(): Flow<String> {
        return flow {
            emit(LocalStorage.getFileName(context))
        }
    }

    override fun getChapters(): Flow<List<Chapter>> {
        return flow {
            LocalStorage.refreshData(context)
            val test = LocalStorage.getTest() ?: emptyList()
            val list = arrayListOf<Chapter>()
            for (entry in test) {
                list.add(Mapper.testChapterToChapter(entry))
            }
            emit(list.toList())
        }
    }

    override fun getQuestions(chapter: Chapter, range: IntRange): List<Question> {
      TODO()
    }

    override fun findQuestion(text: String): Question {
        TODO("Not yet implemented")
    }
}