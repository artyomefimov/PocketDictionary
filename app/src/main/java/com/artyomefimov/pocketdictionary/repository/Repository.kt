package com.artyomefimov.pocketdictionary.repository

import com.artyomefimov.pocketdictionary.LOCAL_STORAGE_PATH
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.convertMapToList
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
class Repository @Inject constructor(
    private val translateApi: TranslateApi,
    private val localStorage: LocalStorage = LocalStorage()
) {

    fun getTranslation(originalWord: String, languagesPair: LanguagePairs): Single<Response> =
        translateApi.getTranslation(originalWord, languagesPair.pair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getDictionary(): Single<List<DictionaryRecord>> {
        if (localStorage.localDictionaryRecords.isNotEmpty()) {
            return Single.fromCallable {
                return@fromCallable convertMapToList(localStorage.localDictionaryRecords)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            return Single.fromCallable {
                readDictionaryFromLocalFile()
                return@fromCallable convertMapToList(localStorage.localDictionaryRecords)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun readDictionaryFromLocalFile() =
        ObjectInputStream(FileInputStream(LOCAL_STORAGE_PATH)).use {
            localStorage.localDictionaryRecords = it.readObject() as MutableMap<String, List<String>>
        }

    fun saveDictionary() =
        ObjectOutputStream(FileOutputStream(LOCAL_STORAGE_PATH)).use {
            it.writeObject(localStorage.localDictionaryRecords)
        }

    fun updateRepository(oldRecord: DictionaryRecord, newRecord: DictionaryRecord) {
        performUpdate(this, oldRecord, newRecord)
    }

    fun getDictionaryRecord(originalWord: String): DictionaryRecord =
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

    fun removeDictionaryRecord(dictionaryRecord: DictionaryRecord) =
        localStorage.removeDictionaryRecord(dictionaryRecord)

    fun removeTranslation(originalWord: String, translation: String) =
        localStorage.removeTranslation(originalWord, translation)
}