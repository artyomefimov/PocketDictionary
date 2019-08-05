package com.artyomefimov.pocketdictionary.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.artyomefimov.pocketdictionary.BaseApp
import com.artyomefimov.pocketdictionary.OLD_DICTIONARY_RECORD
import com.artyomefimov.pocketdictionary.UPDATED_DICTIONARY_RECORD
import com.artyomefimov.pocketdictionary.model.DictionaryRecord

class StorageUpdateService : IntentService(TAG) {
    internal companion object {
        internal const val TAG = "StorageUpdateService"
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null)
            return

        val oldRecord = intent.getSerializableExtra(OLD_DICTIONARY_RECORD) as DictionaryRecord
        val newRecord = intent.getSerializableExtra(UPDATED_DICTIONARY_RECORD) as DictionaryRecord
        val localStorage = (application as BaseApp).localStorage

        Log.d(TAG, "Old record: $oldRecord, updated record: $newRecord")

        performUpdate(localStorage, oldRecord, newRecord)
    }
}