package com.artyomefimov.pocketdictionary.model

import java.io.Serializable

data class DictionaryRecord(
    val originalWord: String,
    val translations: MutableList<String> = ArrayList()
) : Serializable