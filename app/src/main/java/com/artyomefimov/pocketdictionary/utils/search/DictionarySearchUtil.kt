package com.artyomefimov.pocketdictionary.utils.search

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

class DictionarySearchUtil {
    fun search(query: String?, dictionary: List<DictionaryRecord>): List<DictionaryRecord> {
        if (query == null)
            return ArrayList()
        val regex = makeRegex(query).toRegex()
        return dictionary.filter {dictionaryRecord ->
            dictionaryRecord.originalWord.toUpperCase().matches(regex)
        }
    }

    private fun makeRegex(entry: String): String {
        return "[A-Za-z ]*${entry.toUpperCase()}+[A-Za-z ]*"
    }
}