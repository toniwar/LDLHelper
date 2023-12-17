package com.tonivar.ldlhelper

//codes
const val SHOW_PROGRESS = 100
const val HIDE_PROGRESS = 101
const val SET_FILE_NAME = 102
const val FILE_DOWNLOADED = 200
const val FILE_NOT_FOUND = 404

//messages
const val DOWNLOAD_ERROR = "Невозможно загрузить тест из выбранного файла."
const val DOWNLOAD_SUCCESS = "Тест успешно загружен."

//SharedPreferences keys
const val SP_KEY = "LDL_helper_shared_preferences"
const val IS_LOADED = "test_is_loaded"
const val SP_TEST = "sp_test"
const val FILE_NAME = "file_name"