package com.artyomefimov.pocketdictionary.repository

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

fun performUpdate(
    repository: Repository,
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean {
    return when {
        isNewRecordWasCreated(oldRecord, newRecord) -> {
            repository.addDictionaryRecord(newRecord)
            true
        }
        isOnlyTranslationsWereUpdated(oldRecord, newRecord) -> {
            repository.updateTranslations(newRecord)
            true
        }
        isOriginalWordWasChanged(oldRecord, newRecord) -> {
            repository.replaceRecord(oldRecord, newRecord)
            true
        }
        else -> false
    }
}

private fun isNewRecordWasCreated(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean =
    oldRecord.originalWord.isEmpty() && newRecord.originalWord.isNotEmpty()

private fun isOnlyTranslationsWereUpdated(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean =
    (oldRecord.originalWord == newRecord.originalWord) &&
            ((oldRecord.translations != newRecord.translations)
                    || (oldRecord.favoriteTranslations != newRecord.favoriteTranslations))

private fun isOriginalWordWasChanged(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean = oldRecord.originalWord != newRecord.originalWord