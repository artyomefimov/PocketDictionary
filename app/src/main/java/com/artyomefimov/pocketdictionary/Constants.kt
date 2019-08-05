package com.artyomefimov.pocketdictionary

import com.artyomefimov.pocketdictionary.utils.EnvironmentHelper

const val BASE_URL = "https://api.mymemory.translated.net"
val LOCAL_STORAGE_PATH = "${EnvironmentHelper.getExternalStorageDirectory()}/pocketdictionary.backup"

const val EDIT_TRANSLATION_DIALOG_TAG = "edit_translation_dialog"

const val UPDATED_DICTIONARY_RECORD = "updated_dictionary_record"
const val OLD_DICTIONARY_RECORD = "old_dictionary_record"

// request codes
const val PERMISSIONS_REQUEST_CODE = 123
const val EDIT_TRANSLATION_DIALOG_REQUEST_CODE = 124