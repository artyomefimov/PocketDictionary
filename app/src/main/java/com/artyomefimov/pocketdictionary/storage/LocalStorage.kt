package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import java.util.*
import javax.inject.Singleton

@Singleton
class LocalStorage(
    var localDictionaryRecords: MutableMap<String, DictionaryRecord> = TreeMap()
) {
    fun getDictionaryRecord(originalWord: String): DictionaryRecord? {
        if (isNoSuchWordInDictionary(originalWord))
            return DictionaryRecord()

        return dictionaryRecordFromMap(originalWord)
    }

    fun addDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { newRecord ->
            addDictionaryRecord(newRecord)
        }

    fun addDictionaryRecord(dictionaryRecord: DictionaryRecord) =
        localDictionaryRecords.put(
            dictionaryRecord.originalWord,
            dictionaryRecord
        )

    fun updateTranslations(dictionaryRecord: DictionaryRecord) {
        if (isNoSuchWordInDictionary(dictionaryRecord.originalWord))
            return
        replaceTranslations(dictionaryRecord)
    }

    fun replaceRecord(oldRecord: DictionaryRecord, newRecord: DictionaryRecord) {
        removeDictionaryRecord(oldRecord.originalWord)
        addDictionaryRecord(newRecord)
    }

    fun removeDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { record ->
            removeDictionaryRecord(record.originalWord)
        }

    fun removeDictionaryRecord(originalWord: String?) {
        localDictionaryRecords.remove(originalWord)
    }

    fun removeTranslation(originalWord: String, translation: String) {
        if (isNoSuchWordInDictionary(originalWord))
            return
        val updatedTranslations = getMutableListOf(dictionaryRecordFromMap(originalWord)?.translations)
        updatedTranslations.remove(translation)
        dictionaryRecordFromMap(originalWord)?.translations = updatedTranslations
    }

    private fun isNoSuchWordInDictionary(originalWord: String?): Boolean =
        localDictionaryRecords[originalWord] == null

    private fun replaceTranslations(dictionaryRecord: DictionaryRecord) {
        dictionaryRecordFromMap(dictionaryRecord.originalWord)?.translations = dictionaryRecord.translations
        dictionaryRecordFromMap(dictionaryRecord.originalWord)?.favoriteTranslations = dictionaryRecord.favoriteTranslations
    }

    private fun dictionaryRecordFromMap(originalWord: String): DictionaryRecord? =
        localDictionaryRecords[originalWord]
}