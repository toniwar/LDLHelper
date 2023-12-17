package com.tonivar.ldlhelper.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tonivar.ldlhelper.FILE_NAME
import com.tonivar.ldlhelper.IS_LOADED
import com.tonivar.ldlhelper.SP_KEY
import com.tonivar.ldlhelper.SP_TEST
import com.tonivar.ldlhelper.data.models.TestChapter

object LocalStorage {

    private var isLoaded = false
    private var jsonTest: String? = null

    fun downLoadTest(test: List<TestChapter>): Boolean{
        val gson = Gson()
        val typeToken = object : TypeToken<List<TestChapter>>() {}.type
        jsonTest = gson.toJson(test, typeToken)
        isLoaded = jsonTest?.length!! > 50
        Log.d("LocalStorage", jsonTest?:"NuLL")
        Log.d("LocalStorage", isLoaded.toString())
        return isLoaded
    }

    @SuppressLint("CommitPrefEdits")
    fun saveTestToSharedPrefs(context: Context) {
        if(!isLoaded) {
            return
        }
        val sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(SP_TEST, jsonTest)
            putBoolean(IS_LOADED, isLoaded)
            commit()
        }
    }

    fun saveTestFileName(name: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(FILE_NAME, name)
        }
    }

    fun getFileName(context: Context) : String {
        val sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FILE_NAME, "")?: ""
    }

    fun refreshData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        sharedPreferences.apply {
            jsonTest = getString(SP_TEST, null)
            isLoaded = getBoolean(IS_LOADED, false)
        }
    }

    fun getTest(): List<TestChapter>? {
        val gson = Gson()
        val typeToken = object : TypeToken<List<TestChapter>>() {}.type
        return gson.fromJson(jsonTest, typeToken)
    }
}