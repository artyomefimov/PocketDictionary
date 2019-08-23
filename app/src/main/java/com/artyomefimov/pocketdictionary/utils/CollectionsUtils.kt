package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

fun <T>getMutableListOf(immutableList: List<T>) =
    mutableListOf<T>().apply {
        immutableList.forEach { this.add(it) }
    }

fun getTwoFavoriteTranslationsAsString(dictionaryRecord: DictionaryRecord): String {
    return when {
        dictionaryRecord.favoriteTranslations.size == 1 ->
            dictionaryRecord.favoriteTranslations[0]
        dictionaryRecord.favoriteTranslations.size >= 2 ->
            "${dictionaryRecord.favoriteTranslations[0]}, ${dictionaryRecord.favoriteTranslations[1]}"
        else ->
            dictionaryRecord.translations.getOrElse(0) {""}
    }
}