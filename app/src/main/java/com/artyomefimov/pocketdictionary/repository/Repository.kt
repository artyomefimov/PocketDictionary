package com.artyomefimov.pocketdictionary.repository

import com.artyomefimov.pocketdictionary.LOCAL_STORAGE_PATH
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Performs requests to translate API and operates with
 * local storage(reading dictionary from file, CRUD operations with dictionary records)
 */
@Singleton
class Repository @Inject constructor(
    private val translateApi: TranslateApi,
    private val localStorage: LocalStorage,
    private val localFile: File = File(LOCAL_STORAGE_PATH)
) {

    fun getTranslation(originalWord: String, languagesPair: LanguagePairs): Single<Response> =
        translateApi.getTranslation(originalWord, languagesPair.pair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    @Suppress("UNCHECKED_CAST")
    fun getDictionary(): Single<ArrayList<DictionaryRecord>> {
        if (localStorage.localDictionaryRecords.isNotEmpty()) {
            return Single.fromCallable {
                return@fromCallable ArrayList(localStorage.localDictionaryRecords.values)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            return Single.fromCallable {
                readDictionaryFromLocalFile()
                return@fromCallable ArrayList(localStorage.localDictionaryRecords.values)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun readDictionaryFromLocalFile() {
        if (!localFile.exists())
            localFile.createNewFile()

        ObjectInputStream(FileInputStream(localFile)).use {
            localStorage.localDictionaryRecords = it.readObject() as TreeMap<String, DictionaryRecord>
        }
    }

    fun saveDictionary() =
        ObjectOutputStream(FileOutputStream(LOCAL_STORAGE_PATH)).use {
            it.writeObject(localStorage.localDictionaryRecords)
        }

    fun updateDictionaryRecord(oldRecord: DictionaryRecord?, newRecord: DictionaryRecord): Boolean =
        performUpdate(this, oldRecord, newRecord)

    fun getDictionaryRecord(originalWord: String): DictionaryRecord? =
        localStorage.getDictionaryRecord(originalWord)

    fun addDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        localStorage.addDictionaryRecords(dictionaryRecords)

    fun addDictionaryRecord(dictionaryRecord: DictionaryRecord) =
        localStorage.addDictionaryRecord(dictionaryRecord)

    fun updateTranslations(dictionaryRecord: DictionaryRecord) =
        localStorage.updateTranslations(dictionaryRecord)

    fun replaceRecord(oldRecord: DictionaryRecord, newRecord: DictionaryRecord) =
        localStorage.replaceRecord(oldRecord, newRecord)

    fun removeDictionaryRecords(dictionaryRecords: List<DictionaryRecord>) =
        localStorage.removeDictionaryRecords(dictionaryRecords)

    fun removeDictionaryRecord(originalWord: String?) =
        localStorage.removeDictionaryRecord(originalWord)

    fun removeTranslation(originalWord: String, translation: String) =
        localStorage.removeTranslation(originalWord, translation)
}