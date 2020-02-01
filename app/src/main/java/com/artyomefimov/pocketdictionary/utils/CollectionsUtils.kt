package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

/**
 * Converts immutable list to mutable
 */
fun <T>getMutableListOf(immutableList: List<T>?) =
    mutableListOf<T>().apply {
        immutableList?.forEach { this.add(it) }
    }

/**
 * Shows favorite translations for [dictionaryRecord] in the list view
 */
fun getTwoFavoriteTranslationsAsString(dictionaryRecord: DictionaryRecord?): String {
    dictionaryRecord?.let {
        return when {
            dictionaryRecord.favoriteTranslations.size == 1 ->
                dictionaryRecord.favoriteTranslations[0]
            dictionaryRecord.favoriteTranslations.size >= 2 ->
                "${dictionaryRecord.favoriteTranslations[0]}, ${dictionaryRecord.favoriteTranslations[1]}"
            else ->
                dictionaryRecord.translations.getOrElse(0) {""}
        }
    } ?: return ""
}