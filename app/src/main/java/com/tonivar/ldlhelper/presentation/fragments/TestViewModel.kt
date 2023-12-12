package com.tonivar.ldlhelper.presentation.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonivar.ldlhelper.data.LDLTestRepositoryImpl
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.UserData
import java.io.File

class TestViewModel: ViewModel() {

    private val repository by lazy {
        LDLTestRepositoryImpl()
    }
    private val chapters = arrayListOf<Chapter>()

    private val mutableData = MutableLiveData<List<UserData>>()
    val data: LiveData<List<UserData>> get() = mutableData

    fun loadTest(file: File?){
        file?.let {
            repository.getTest(it)
            loadChapters()
            loadQuestions()
        }
    }
    private fun loadChapters() {
        repository.getChapters().forEach {
            chapters.add(it)
        }
        mutableData.value = chapters.toList()
    }

    private fun loadQuestions() {
        chapters.forEach{
            mutableData.value = repository.getQuestions(it, 10..21)
        }

    }

}