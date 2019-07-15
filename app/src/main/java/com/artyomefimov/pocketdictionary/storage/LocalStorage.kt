package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.LOCAL_STORAGE_PATH
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject

class LocalStorage @Inject constructor() {

    companion object {
        private const val NO_SUCH_WORD = "No such word"
    }

    internal val localDictionaryRecords: MutableMap<String, MutableList<String>> = HashMap()

    fun getDictionaryRecords(): List<DictionaryRecord> {
        val result = ArrayList<DictionaryRecord>()
        localDictionaryRecords.forEach {
            result.add(DictionaryRecord(it.key, it.value))
        }
        return result
    }

    fun getDictionaryRecord(originalWord: String): DictionaryRecord {
        if (localDictionaryRecords[originalWord] == null)
            throw Exception("$NO_SUCH_WORD: $originalWord")

        return DictionaryRecord(originalWord, localDictionaryRecords[originalWord]!!)
    }

    fun addDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { newRecord ->
            addDictionaryRecord(newRecord)
        }

    fun addDictionaryRecord(dictionaryRecord: DictionaryRecord) {
        if (this.localDictionaryRecords[dictionaryRecord.originalWord] == null)
            this.localDictionaryRecords[dictionaryRecord.originalWord] = dictionaryRecord.translations
        else {
            val currentTranslations = this.localDictionaryRecords[dictionaryRecord.originalWord]!!
            dictionaryRecord.translations.forEach { translation ->
                if (!currentTranslations.contains(translation))
                    currentTranslations.add(translation)
            }
        }
    }

    fun removeDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        dictionaryRecords.forEach { record ->
            removeDictionaryRecord(record)
        }

    fun removeDictionaryRecord(dictionaryRecord: DictionaryRecord) {
        if (localDictionaryRecords[dictionaryRecord.originalWord] == null)
            return

        localDictionaryRecords.remove(dictionaryRecord.originalWord)
    }

    fun removeTranslation(originalWord: String, translation: String) {
        localDictionaryRecords[originalWord]?.remove(translation)
            ?: throw Exception("$NO_SUCH_WORD: $originalWord")
    }

    fun writeData() =
        ObjectOutputStream(FileOutputStream(LOCAL_STORAGE_PATH)).use {
            it.writeObject(localDictionaryRecords)
        }

    fun readData(): MutableMap<String, MutableList<String>> =
        ObjectInputStream(FileInputStream(LOCAL_STORAGE_PATH)).use {
            return it.readObject() as MutableMap<String, MutableList<String>>
        }
}