package com.artyomefimov.pocketdictionary.services

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.repository.performUpdate
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class StorageUpdateHelperKtTest {
    @Mock
    private lateinit var repository: Repository

    @Test
    fun testRecordIsAddedIfOldRecordWasEmpty() {
        val oldRecord = DictionaryRecord()
        val newRecord = DictionaryRecord("some word")
        performUpdate(repository, oldRecord, newRecord)

        verify(repository).addDictionaryRecord(eq(newRecord))
    }

    @Test
    fun testTranslationsAreUpdatedIfOriginalWordsAreEqual() {
        val oldRecord = DictionaryRecord("equal word")
        val newRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })
        performUpdate(repository, oldRecord, newRecord)

        verify(repository).updateTranslations(eq(newRecord))
    }

    @Test
    fun testNothingIsHappenedIfRecordsAreEqual() {
        val oldRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })
        val newRecord = DictionaryRecord("equal word", ArrayList<String>().apply { add("any") })

        performUpdate(repository, oldRecord, newRecord)

        verify(repository, never()).addDictionaryRecord(any())
        verify(repository, never()).updateTranslations(any())
        verify(repository, never()).replaceRecord(any(), any())
    }

    @Test
    fun testRecordIsReplacedIfOriginalWordsAreDifferent() {
        val oldRecord = DictionaryRecord("not equal word")
        val newRecord = DictionaryRecord("equal word")
        performUpdate(repository, oldRecord, newRecord)

        verify(repository).replaceRecord(eq(oldRecord), eq(newRecord))
    }
}