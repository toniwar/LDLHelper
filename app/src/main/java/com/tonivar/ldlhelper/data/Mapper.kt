package com.tonivar.ldlhelper.data

import com.tonivar.ldlhelper.data.models.TestAnswer
import com.tonivar.ldlhelper.data.models.TestChapter
import com.tonivar.ldlhelper.data.models.TestQuestion
import com.tonivar.ldlhelper.domain.models.Answer
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question

object Mapper {

    fun testChapterToChapter(tChapter : TestChapter) : Chapter {
        return Chapter(tChapter.id, tChapter.title)
    }

    fun testQuestionToQuestion(tQuestion : TestQuestion) : Question {
        val answers = arrayListOf<Answer>()
        for (tAnswer in tQuestion.answers) {
            answers.add(testAnswerToAnswer(tAnswer))
        }
        return Question(tQuestion.id, tQuestion.question, answers)
    }

    private fun testAnswerToAnswer(tAnswer : TestAnswer) : Answer {
        return Answer(tAnswer.answer, tAnswer.isRight)
    }
}