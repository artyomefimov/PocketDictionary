package com.artyomefimov.pocketdictionary.repository

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

/**
 * Detects changes in a dictionary record by comparing its old and new versions and
 * delegates to the repository the execution of the specified action based on the detection result
 * @return [true] if the action was delegated to the repository, [false] otherwise
 */
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
): Boolean {
    val originalWordsAreEqual = oldRecord.originalWord == newRecord.originalWord
    val translationsWereChanged = oldRecord.translations != newRecord.translations
    val favoriteTranslationsWereChanged = oldRecord.favoriteTranslations != newRecord.favoriteTranslations

    return originalWordsAreEqual and (translationsWereChanged or favoriteTranslationsWereChanged)
}

private fun isOriginalWordWasChanged(
    oldRecord: DictionaryRecord,
    newRecord: DictionaryRecord
): Boolean = oldRecord.originalWord != newRecord.originalWord