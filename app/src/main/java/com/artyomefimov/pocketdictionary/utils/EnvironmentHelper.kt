package com.artyomefimov.pocketdictionary.utils

import android.os.Environment

/**
 * For mocking
 */
object EnvironmentHelper {
    fun getExternalStorageDirectory(): String = Environment.getExternalStorageDirectory().absolutePath
}