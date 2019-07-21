package com.artyomefimov.pocketdictionary

import com.artyomefimov.pocketdictionary.utils.EnvironmentHelper

const val BASE_URL = "https://api.mymemory.translated.net"
val LOCAL_STORAGE_PATH = "${EnvironmentHelper.getExternalStorageDirectory()}/pocketdictionary.backup"

const val EDIT_TRANSLATION_DIALOG_TAG = "edit_translation_dialog"

// request codes
const val EDIT_TRANSLATION_DIALOG_REQUEST_CODE = 124