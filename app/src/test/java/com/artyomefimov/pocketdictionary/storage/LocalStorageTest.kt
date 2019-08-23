package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocalStorageTest {
    private val storage = LocalStorage()

    @Before
    fun setUp() {
        storage.localDictionaryRecords["dog"] = DictionaryRecord(
            "dog",
            listOf("собака", "пес", "песель"))
        storage.localDictionaryRecords["cat"] = DictionaryRecord(
            "cat",
            listOf("кошка", "кошак", "киса"))
        storage.localDictionaryRecords["bird"] = DictionaryRecord(
            "bird",
            listOf("птица", "птичка"))
    }

    @Test
    fun testGettingTranslationsForNotExistingWord() {
        assertEquals(DictionaryRecord(), storage.getDictionaryRecord("qwerty"))
    }

    @Test
    fun testGettingTranslationsForExistingWord() {
        assertEquals(3, storage.getDictionaryRecord("dog").translations.size)
    }

    @Test
    fun testAddNewDictionaryRecord() {
        val newRecord = DictionaryRecord(
                "wolf",
                ArrayList<String>().apply { add("wolf") }
            )

        storage.addDictionaryRecord(newRecord)

        assertEquals(newRecord, storage.getDictionaryRecord(newRecord.originalWord))
    }

    @Test
    fun testAddNewTranslationsToExistingDictionaryRecord() {
        val newRecordWithNewTranslation = DictionaryRecord(
            "dog",
            ArrayList<String>().apply { add("собачка") }
        )

        storage.updateTranslations(newRecordWithNewTranslation)

        assertTrue(storage.localDictionaryRecords["dog"]!!.translations.contains("собачка"))
    }

    @Test
    fun testDuplicatingTranslationIsNotAddedToExistingDictionaryRecord() {
        val newRecordWithNewAndExistingTranslation = DictionaryRecord(
            "dog",
            ArrayList<String>().apply {
                add("собачка")
                add("собака")
            }
        )

        storage.updateTranslations(newRecordWithNewAndExistingTranslation)

        assertTrue(storage.getDictionaryRecord("dog")
            .translations.contains("собачка"))

        assertEquals(1,
            storage.getDictionaryRecord("dog")
            .translations.count { it == "собака" })

        assertEquals(newRecordWithNewAndExistingTranslation.translations,
            storage.getDictionaryRecord("dog").translations)
    }

    @Test
    fun testRemoveExistingDictionaryRecord() {
        storage.removeDictionaryRecord("dog")

        try {
            storage.getDictionaryRecord("dog")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @Test
    fun testRemoveTranslationForNonExistingRecord() {
        storage.removeTranslation("qwerty", "2")
    }

    @Test
    fun testRemoveTranslationForExistingRecord() {
        storage.removeTranslation("cat", "кошак")

        assertFalse(storage.getDictionaryRecord("cat")
            .translations.contains("кошак"))
    }
}