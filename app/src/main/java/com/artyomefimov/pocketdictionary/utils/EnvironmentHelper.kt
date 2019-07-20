package com.artyomefimov.pocketdictionary.utils

import android.os.Environment

object EnvironmentHelper {
    fun getExternalStorageDirectory(): String = Environment.getExternalStorageDirectory().absolutePath
}