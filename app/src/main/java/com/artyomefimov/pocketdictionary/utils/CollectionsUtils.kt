package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

fun <T>getMutableListOf(immutableList: List<T>) =
    mutableListOf<T>().apply {
        immutableList.forEach { this.add(it) }
    }

fun convertMapToList(map: Map<String, List<String>>): List<DictionaryRecord> =
    map.map {
        DictionaryRecord(it.key, it.value)
    }