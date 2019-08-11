package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun search(query: String?, dictionary: List<DictionaryRecord>): Single<List<DictionaryRecord>> =
    Single.fromCallable {
        return@fromCallable getSearchResult(query, dictionary)
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

internal fun getSearchResult(query: String?, dictionary: List<DictionaryRecord>): List<DictionaryRecord> {
    if (query == null)
        return ArrayList()

    val regex = makeRegex(query).toRegex()
    return dictionary.filter { dictionaryRecord ->
        dictionaryRecord.originalWord.toUpperCase().matches(regex)
    }
}

private fun makeRegex(entry: String): String {
    return "[A-Za-z ]*${entry.toUpperCase()}+[A-Za-z ]*"
}