package com.tonivar.ldlhelper.presentation.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tonivar.ldlhelper.HIDE_PROGRESS
import com.tonivar.ldlhelper.SET_FILE_NAME
import com.tonivar.ldlhelper.SHOW_PROGRESS
import com.tonivar.ldlhelper.data.LDLTestRepositoryImpl
import com.tonivar.ldlhelper.domain.models.AppResponse
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Response
import com.tonivar.ldlhelper.domain.models.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy {
        LDLTestRepositoryImpl(application)
    }
    private val scope1 = CoroutineScope(Dispatchers.IO)
    private val scope2 = CoroutineScope(Dispatchers.IO)
    private val chapters = arrayListOf<Chapter>()
    private val mResponseFlow = MutableStateFlow<Response?>(null)
    val responseFlow: StateFlow<Response?> get() = mResponseFlow.asStateFlow()

    private val mutableData = MutableLiveData<List<UserData>>()

    private val mUserDataFlow = MutableStateFlow<List<UserData>?>(null)
    val userDataFlow: StateFlow<List<UserData>?> get() = mUserDataFlow.asStateFlow()
    val data: LiveData<List<UserData>> get() = mutableData

    fun setFileName(name: String) {
        repository.saveTestFileName(name)
    }

    private fun getFileName() {
        repository.getTestFileName().onEach {
            val response = AppResponse(SET_FILE_NAME, it)
            mResponseFlow.value = response
        }.launchIn(scope1)
    }

    fun loadTest(source : List<String>?){
        progressBarAction(SHOW_PROGRESS)
        source?.let {
            Log.d("TestViewModel", source[0])
            scope1.launch{
                val result = repository.getTest(it)
                mResponseFlow.value = result.first()
            }
        }
    }
    fun loadChapters(showProgressBar: Boolean = false) {
        if (showProgressBar){
            progressBarAction(SHOW_PROGRESS)
        }
        getFileName()
        chapters.clear()
        repository.getChapters().onEach { list ->
            for (item in list) {
                chapters.add(item)
            }
            mUserDataFlow.value = chapters.toList().also {
                progressBarAction(HIDE_PROGRESS)
            }
        }.launchIn(scope2)
    }

    private fun progressBarAction(code: Int) {
        mResponseFlow.value = AppResponse(code, "")
    }

    private fun loadQuestions() {
        chapters.forEach{
            mutableData.value = repository.getQuestions(it, 9..12)
        }

    }

    override fun onCleared() {
        super.onCleared()
        scope1.cancel()
        scope2.cancel()
    }

}