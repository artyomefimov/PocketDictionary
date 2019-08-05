package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.LOCAL_STORAGE_PATH
import com.artyomefimov.pocketdictionary.di.application.ApplicationScope
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject

@ApplicationScope
class LocalStorage @Inject constructor() {
    internal var localDictionaryRecords: MutableMap<String, List<String>> = HashMap()

    fun loadDictionary(): Single<List<DictionaryRecord>> { // todo read from file in the beginning of the work
        return Single.fromCallable {
            localDictionaryRecords = readDictionaryFromLocalFile()
            return@fromCallable localDictionaryRecords.map {
                DictionaryRecord(it.key, it.value)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

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
        addMissingTranslationsForExistingWord(dictionaryRecord)
    }

    private fun isNoSuchWordInDictionary(originalWord: String): Boolean =
        localDictionaryRecords[originalWord] == null

    private fun addMissingTranslationsForExistingWord(dictionaryRecord: DictionaryRecord) {
        localDictionaryRecords[dictionaryRecord.originalWord] = dictionaryRecord.translations
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

    fun writeDictionaryToLocalFile() =
        ObjectOutputStream(FileOutputStream(LOCAL_STORAGE_PATH)).use {
            it.writeObject(localDictionaryRecords)
        }

    fun readDictionaryFromLocalFile(): MutableMap<String, List<String>> =
        ObjectInputStream(FileInputStream(LOCAL_STORAGE_PATH)).use {
            return@use it.readObject() as MutableMap<String, List<String>>
        }
}