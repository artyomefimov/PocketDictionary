package com.artyomefimov.pocketdictionary.storage

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocalStorageTest {
    private val storage = LocalStorage()

    @Before
    fun setUp() {
        storage.localDictionaryRecords["dog"] = ArrayList<String>().apply {
            add("собака")
            add("пес")
            add("песель")
        }
        storage.localDictionaryRecords["cat"] = ArrayList<String>().apply {
            add("кошка")
            add("кошак")
            add("киса")
        }
        storage.localDictionaryRecords["bird"] = ArrayList<String>().apply {
            add("птица")
            add("птичка")
        }
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

        assertTrue(storage.localDictionaryRecords["dog"]!!.contains("собачка"))
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