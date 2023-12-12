package com.tonivar.ldlhelper.data

import com.tonivar.ldlhelper.data.models.TestAnswer
import com.tonivar.ldlhelper.data.models.TestChapter
import com.tonivar.ldlhelper.data.models.TestQuestion
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File

class TestProvider {

    fun getExcelFileFromStorage(file: File): Map<TestChapter, List<TestQuestion>> {
        val wb = try {
            WorkbookFactory.create(file.absoluteFile)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        var counter = 0
        val testFileMap = HashMap<TestChapter, List<TestQuestion>>()
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
                    row.getCell(0).stringCellValue
                } catch (e: IllegalStateException) {
                    row.getCell(0).numericCellValue.toString()
                } catch (e: NullPointerException) {
                    ""
                }
                if (type.equals("в", true)) {
                    id++
                    list.add(TestQuestion(id, text))

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
            testFileMap[TestChapter(counter, sheet.sheetName)] = list.toList()
            counter++
        }
        return testFileMap.toMap()
    }
}
