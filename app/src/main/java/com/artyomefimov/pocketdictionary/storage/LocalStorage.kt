package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.getMutableListOf

class LocalStorage(
    var localDictionaryRecords: MutableMap<String, List<String>> = HashMap()
) {
    fun getDictionaryRecord(originalWord: String): DictionaryRecord {
        if (isNoSuchWordInDictionary(originalWord))
            return DictionaryRecord()

        return DictionaryRecord(originalWord, localDictionaryRecords[originalWord]!!)
    }

    fun addDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { newRecord ->
            addDictionaryRecord(newRecord)
        }

    fun addDictionaryRecord(dictionaryRecord: DictionaryRecord) =
        localDictionaryRecords.put(
            dictionaryRecord.originalWord,
            dictionaryRecord.translations as MutableList<String>
        )

    fun updateTranslations(dictionaryRecord: DictionaryRecord) {
        if (isNoSuchWordInDictionary(dictionaryRecord.originalWord))
            return
        replaceTranslations(dictionaryRecord)
    }

    fun replaceRecord(oldRecord: DictionaryRecord, newRecord: DictionaryRecord) {
        removeDictionaryRecord(oldRecord)
        addDictionaryRecord(newRecord)
    }

    fun removeDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { record ->
            removeDictionaryRecord(record)
        }

    fun removeDictionaryRecord(dictionaryRecord: DictionaryRecord) {
        if (isNoSuchWordInDictionary(dictionaryRecord.originalWord))
            return

        localDictionaryRecords.remove(dictionaryRecord.originalWord)
    }

    fun removeTranslation(originalWord: String, translation: String) {
        if (isNoSuchWordInDictionary(originalWord))
            return
        val updatedTranslations = getMutableListOf(localDictionaryRecords[originalWord]!!)
        updatedTranslations.remove(translation)
        localDictionaryRecords[originalWord] = updatedTranslations
    }

    private fun isNoSuchWordInDictionary(originalWord: String): Boolean =
        localDictionaryRecords[originalWord] == null

    private fun replaceTranslations(dictionaryRecord: DictionaryRecord) {
        localDictionaryRecords[dictionaryRecord.originalWord] = dictionaryRecord.translations
    }
}