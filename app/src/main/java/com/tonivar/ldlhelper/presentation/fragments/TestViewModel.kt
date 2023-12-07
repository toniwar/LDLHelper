package com.tonivar.ldlhelper.presentation.fragments

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tonivar.ldlhelper.data.LDLTestRepositoryImpl
import com.tonivar.ldlhelper.domain.models.Answer
import com.tonivar.ldlhelper.domain.models.Question
import com.tonivar.ldlhelper.domain.models.UserData

class TestViewModel: ViewModel() {

    private val repository by lazy {
        LDLTestRepositoryImpl()
    }

    private val mutableData = MutableLiveData<UserData>()
    val data: LiveData<UserData> get() = mutableData

    fun loadTest(activity: Activity){
        repository.getTest(activity)
    }

}