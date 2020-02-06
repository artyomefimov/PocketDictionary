package com.artyomefimov.pocketdictionary.repository

import android.content.SharedPreferences
import android.util.Log
import com.artyomefimov.pocketdictionary.DICTIONARY_KEY
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.model.Dictionary
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.genericType
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    private val gson: Gson,
    private val tag: String = Repository::javaClass.name
) {

    fun getTranslation(originalWord: String, languagesPair: LanguagePairs): Single<Response> =
        translateApi.getTranslation(originalWord, languagesPair.pair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    @Throws(ReadDictionaryException::class)
    fun getDictionary(sharedPreferences: SharedPreferences?): Single<ArrayList<DictionaryRecord>> {
        if (localStorage.localDictionaryRecords.isNotEmpty()) {
            return Single.fromCallable {
                return@fromCallable ArrayList(localStorage.localDictionaryRecords.values)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        } else {
            return Single.fromCallable {
                val dictionary = readDictionaryFromLocalFile(sharedPreferences)
                localStorage.localDictionaryRecords = dictionary.localDictionaryRecords
                return@fromCallable ArrayList(localStorage.localDictionaryRecords.values)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @Throws(ReadDictionaryException::class)
    private fun readDictionaryFromLocalFile(sharedPreferences: SharedPreferences?): Dictionary {
        val dictionaryJson = sharedPreferences?.getString(DICTIONARY_KEY, "") ?: return Dictionary()
        Log.i(tag, "Read json string: $dictionaryJson")
        if (dictionaryJson.isEmpty())
            return Dictionary()

        try {
            return gson.fromJson<Dictionary>(dictionaryJson, genericType<Dictionary>())
        } catch (e: JsonSyntaxException) {
            Log.e(tag, e.message, e)
            throw ReadDictionaryException()
        }
    }

    fun saveDictionary(sharedPreferences: SharedPreferences) {
        val dictionary = gson.toJson(Dictionary(localStorage.localDictionaryRecords))
        sharedPreferences.edit().apply {
            putString(DICTIONARY_KEY, dictionary)
            apply()
        }
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