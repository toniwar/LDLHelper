package com.tonivar.ldlhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File

object URIPathExtractor {

    @SuppressLint("Recycle")
    fun getPathFromUri(uri: Uri) : String {
        return uri.path?.replace("document%3A", "").toString()
    }
}