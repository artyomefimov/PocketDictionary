package com.artyomefimov.pocketdictionary.services

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class StorageUpdateHelperKtTest {
    @Mock
    private lateinit var localStorage: LocalStorage

    @Before
    fun setUp() {
        doNothing().whenever(localStorage).writeDictionaryToLocalFile()
    }

    @Test
    fun testRecordIsAddedIfOldRecordWasEmpty() {
        val oldRecord = DictionaryRecord()
        val newRecord = DictionaryRecord("some word")
        performUpdate(localStorage, oldRecord, newRecord)

        verify(localStorage).addDictionaryRecord(eq(newRecord))
    }

    @Test
    fun testTranslationsAreUpdatedIfOriginalWordsAreEqual() {
        val oldRecord = DictionaryRecord("equal word")
        val newRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })
        performUpdate(localStorage, oldRecord, newRecord)

        verify(localStorage).updateTranslations(eq(newRecord))
    }

    @Test
    fun testNothingIsHappenedIfRecordsAreEqual() {
        val oldRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })
        val newRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })

        performUpdate(localStorage, oldRecord, newRecord)

        verify(localStorage, never()).addDictionaryRecord(any())
        verify(localStorage, never()).updateTranslations(any())
        verify(localStorage, never()).replaceRecord(any(), any())
    }

    @Test
    fun testRecordIsReplacedIfOriginalWordsAreDifferent() {
        val oldRecord = DictionaryRecord("not equal word")
        val newRecord = DictionaryRecord("equal word")
        performUpdate(localStorage, oldRecord, newRecord)

        verify(localStorage).replaceRecord(eq(oldRecord), eq(newRecord))
    }
}