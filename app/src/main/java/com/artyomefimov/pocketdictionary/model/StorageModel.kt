package com.artyomefimov.pocketdictionary.model

import java.io.Serializable

data class DictionaryRecord(
    val originalWord: String = "",
    var translations: List<String> = ArrayList(),
    var favoriteTranslations: MutableList<String> = ArrayList()
) : Serializable