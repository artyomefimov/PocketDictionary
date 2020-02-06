package com.artyomefimov.pocketdictionary.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Dictionary(
    val localDictionaryRecords: MutableMap<String, DictionaryRecord> = TreeMap()
)
/**
 * Model class that describes original word, its translations and favorite translations
 */
data class DictionaryRecord(
    var originalWord: String = "",
    var translations: List<String> = ArrayList(),
    var favoriteTranslations: MutableList<String> = ArrayList()
) : Serializable