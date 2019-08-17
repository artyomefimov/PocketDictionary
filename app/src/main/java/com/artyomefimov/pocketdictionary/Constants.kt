package com.artyomefimov.pocketdictionary

import com.artyomefimov.pocketdictionary.utils.EnvironmentHelper

const val BASE_URL = "https://api.mymemory.translated.net"
val LOCAL_STORAGE_PATH = "${EnvironmentHelper.getExternalStorageDirectory()}/pocketdictionary.backup"

const val EDIT_TRANSLATION_DIALOG_TAG = "edit_translation_dialog"
const val NEW_TRANSLATION_POSITION = -2

const val CONFIRM_DELETION_DIALOG_TAG = "confirm_deletion_dialog"

// request codes
const val PERMISSIONS_REQUEST_CODE = 123
const val EDIT_TRANSLATION_DIALOG_REQUEST_CODE = 124
const val CONFIRM_DELETION_DIALOG_REQUEST_CODE = 125