package com.tonivar.ldlhelper.data

import android.util.Log
import com.tonivar.ldlhelper.data.models.TestAnswer
import com.tonivar.ldlhelper.data.models.TestChapter
import com.tonivar.ldlhelper.data.models.TestQuestion
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
class TestProvider {

    fun getExcelFileFromStorage(source: List<String>): List<TestChapter> {
        if (source.isEmpty()) {
            return emptyList()
        }
        var wb: Workbook? = null
        for (path in source) {
            wb = createWorkBook(path)
            if (wb != null) {
                break
            }
        }
        if (wb == null) {
            return emptyList()
        }

        var counter = 0
        val resultList = ArrayList<TestChapter>()
        for (sheet in wb) {
            val list = ArrayList<TestQuestion>()
            var id = -1
            for (row in sheet) {
                val type = try {
                    row.getCell(0).stringCellValue
                } catch (e: IllegalStateException) {
                    row.getCell(0).numericCellValue.toString()
                } catch (e: NullPointerException) {
                    ""
                }
                val text = try {
                    row.getCell(1).stringCellValue
                } catch (e: IllegalStateException) {
                    row.getCell(1).numericCellValue.toString()
                } catch (e: NullPointerException) {
                    ""
                }
                if (type.equals("в", true)) {
                    id++
                    list.add(TestQuestion(id, question = text))

                }
                if (type.equals("о", true)) {
                    list[id].answers.add(TestAnswer(text, false))
                }
                if (type.equals("п", true)) {
                    list[id].answers.add(TestAnswer(text, true))
                }
            }
            if (list.isEmpty()) {
                continue
            }
            resultList.add(TestChapter(counter, list.toList(), sheet.sheetName))
            counter++
        }
        return resultList.toList()
    }

    private fun createWorkBook(path: String) : Workbook? {
        return try {
            WorkbookFactory.create(File(path))
        } catch (e: Exception) {
            Log.e("TestProvider", "$e")
            null
        }
    }
}
