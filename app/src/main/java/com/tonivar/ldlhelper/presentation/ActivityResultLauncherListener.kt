package com.tonivar.ldlhelper.presentation

import android.net.Uri

interface ActivityResultLauncherListener {
    fun launch(fileType : String, callback: ((Uri) -> Unit)?)
}