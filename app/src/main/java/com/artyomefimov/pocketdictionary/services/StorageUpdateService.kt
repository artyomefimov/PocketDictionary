package com.artyomefimov.pocketdictionary.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.artyomefimov.pocketdictionary.PocketDictionaryApplication

/**
 * Saves current dictionary to a local file in a background
 */
class StorageUpdateService : IntentService(TAG) {
    private companion object {
        private const val TAG = "StorageUpdateService"
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null)
            return

        try {
            PocketDictionaryApplication.repository(this).saveDictionary()
        } catch (e: Exception) {
            Log.e(TAG, "Could not save dictionary to file", e)
        }
    }
}