package com.artyomefimov.pocketdictionary.repository

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

fun performUpdate(repository: Repository, oldRecord: DictionaryRecord, newRecord: DictionaryRecord) {
    when {
        isNewRecordWasCreated(oldRecord) -> repository.addDictionaryRecord(newRecord)
        isOnlyTranslationsWereUpdated(oldRecord, newRecord) -> repository.updateTranslations(newRecord)
        isOriginalWordWasChanged(oldRecord, newRecord) -> repository.replaceRecord(oldRecord, newRecord)
        else -> return
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