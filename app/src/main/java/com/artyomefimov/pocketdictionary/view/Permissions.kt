package com.artyomefimov.pocketdictionary.view

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

val needed_permissions = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

fun isPermissionsGranted(context: Activity, permissions: Array<String>): Boolean {
    var isAllGranted = true
    permissions.forEach { permission ->
        isAllGranted = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    return isAllGranted
}