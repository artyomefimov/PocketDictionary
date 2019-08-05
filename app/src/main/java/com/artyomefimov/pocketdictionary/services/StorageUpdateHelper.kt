package com.artyomefimov.pocketdictionary.services

import android.util.Log
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage

fun performUpdate(localStorage: LocalStorage, oldRecord: DictionaryRecord, newRecord: DictionaryRecord) {
    when {
        isNewRecordWasCreated(oldRecord) -> localStorage.addDictionaryRecord(newRecord)
        isOnlyTranslationsWereUpdated(oldRecord, newRecord) -> localStorage.updateTranslations(newRecord)
        isOriginalWordWasChanged(oldRecord, newRecord) -> localStorage.replaceRecord(oldRecord, newRecord)
        else -> return
    }

    try {
        localStorage.writeDictionaryToLocalFile()
    } catch (e: Exception) {
        Log.e(StorageUpdateService.TAG, "Could not write dictionary to file", e)
    }
}

private fun isNewRecordWasCreated(oldRecord: DictionaryRecord): Boolean =
    oldRecord.originalWord.isEmpty()

private fun isOnlyTranslationsWereUpdated(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean =
    (oldRecord.originalWord == newRecord.originalWord) &&
            (oldRecord.translations != newRecord.translations)

private fun isOriginalWordWasChanged(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean = oldRecord.originalWord != newRecord.originalWord