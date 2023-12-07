package com.tonivar.ldlhelper.data

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.net.toFile
import com.tonivar.ldlhelper.presentation.MainActivity
import java.io.File
import java.lang.Exception

class TestProvider {

    fun getExcelFileFromStorage(activity: Activity): File? {
        var file: File? = null
        val mimeTypes = arrayOf("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType( "*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            activity.startActivity(Intent.createChooser(intent, "Выберите тест"))
        } catch (e: Exception) {
            Log.e("TestProvider", "Exception: $e: ${e.message}")
            return null
        }
        (activity as MainActivity).activityResultLauncher = {uri ->
            if (uri == null) {
               Log.d("TestProvider", "$uri")
            }
            uri?.let {
                file = it.toFile()
                Log.d("TestProvider", "${file!!.absoluteFile}")
            }
        }
        return file
    }
}