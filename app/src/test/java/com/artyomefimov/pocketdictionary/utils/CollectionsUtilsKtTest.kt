package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import org.junit.Assert.*
import org.junit.Test

class FavoriteTranslationsAsStringTest {
    @Test
    fun testDictionaryRecordWithEmptyTranslationsReturnsEmptyString() {
        val dictionaryRecord = DictionaryRecord("Some name")
        assertEquals("", getTwoFavoriteTranslationsAsString(dictionaryRecord))
    }

    @Test
    fun testDictionaryRecordWithNonEmptyTranslationsReturnsFirstElement() {
        val dictionaryRecord = DictionaryRecord(
            "name",
            translations = listOf("1", "2"))
        assertEquals("1", getTwoFavoriteTranslationsAsString(dictionaryRecord))
    }

    @Test
    fun testDictionaryRecordWithOneFavoriteTranslationReturnsIt() {
        val dictionaryRecord = DictionaryRecord(
            "name",
            translations = listOf("1", "2"),
            favoriteTranslations = mutableListOf("first")
        )
        assertEquals("first", getTwoFavoriteTranslationsAsString(dictionaryRecord))
    }

    @Test
    fun testDictionaryRecordWithMoreThanOneTranslationsReturnsFirstTwoWithComma() {
        val dictionaryRecord = DictionaryRecord(
            "name",
            translations = listOf("1", "2"),
            favoriteTranslations = mutableListOf("first", "second", "third")
        )
        assertEquals("first, second", getTwoFavoriteTranslationsAsString(dictionaryRecord))
    }
}