package com.artyomefimov.pocketdictionary.model

import java.io.Serializable

/**
 * Model class that describes original word, its translations and favorite translations
 */
data class DictionaryRecord(
    var originalWord: String = "",
    var translations: List<String> = ArrayList(),
    var favoriteTranslations: MutableList<String> = ArrayList()
) : Serializable