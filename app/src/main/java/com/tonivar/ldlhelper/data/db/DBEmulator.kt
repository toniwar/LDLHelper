package com.tonivar.ldlhelper.data.db

import com.tonivar.ldlhelper.data.models.TestChapter
import com.tonivar.ldlhelper.data.models.TestQuestion

object DBEmulator  {
    private val testMap = HashMap<TestChapter, List<TestQuestion>>()

    fun fillTestList(map : Map<TestChapter, List<TestQuestion>>) {
        for (chapter in map) {
            testMap[chapter.key] = chapter.value
        }
    }
    fun getTestMap(): Map<TestChapter, List<TestQuestion>> {
        return testMap.toMap()
    }
}