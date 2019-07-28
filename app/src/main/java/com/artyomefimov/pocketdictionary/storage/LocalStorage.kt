package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.LOCAL_STORAGE_PATH
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage @Inject constructor() {

    companion object {
        private const val NO_SUCH_WORD = "No such word"
    }

    internal var localDictionaryRecords: MutableMap<String, MutableList<String>> = HashMap()

    fun loadDictionary(): Single<List<DictionaryRecord>> {
        return Single.fromCallable {
            localDictionaryRecords = readDictionaryFromFile()
            return@fromCallable localDictionaryRecords.map {
                DictionaryRecord(it.key, it.value)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getDictionaryRecord(originalWord: String): DictionaryRecord {
        if (localDictionaryRecords[originalWord] == null)
            return DictionaryRecord()

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

    internal fun readDictionaryFromFile(): MutableMap<String, MutableList<String>> =
        ObjectInputStream(FileInputStream(LOCAL_STORAGE_PATH)).use {
            return@use it.readObject() as MutableMap<String, MutableList<String>>
        }
}