package com.tonivar.ldlhelper.presentation

import android.net.Uri

interface ActivityResultLauncherListener {
    fun openFile(fileType : String, callback: ((Uri) -> Unit)?)
    fun<T> requestPermissions(request: T)
}
