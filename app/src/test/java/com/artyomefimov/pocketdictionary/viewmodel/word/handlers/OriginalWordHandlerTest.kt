package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OriginalWordHandlerTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var originalWordHandler: OriginalWordHandler

    @Before
    fun setUp() {
        originalWordHandler = OriginalWordHandler(repository)

        val dog = DictionaryRecord(
            "dog",
            ArrayList<String>().apply {
                add("собака")
                add("пес")
                add("песель")
            })
        val cat = DictionaryRecord(
            "cat",
            ArrayList<String>().apply {
                add("кошка")
                add("кошак")
                add("киса")
            }
        )
        val emptyWord = DictionaryRecord()

        repository.apply {
            addDictionaryRecord(dog)
            addDictionaryRecord(cat)
        }

        whenever(repository.getDictionaryRecord(eq("dog"))).thenReturn(dog)
        whenever(repository.getDictionaryRecord(eq("tiger"))).thenReturn(emptyWord)
    }

    @Test
    fun testForIncorrectLatinInput() {
        val result = originalWordHandler.handle("cat1", "cat")
        assertTrue(result is Result.LatinInputIncorrect)
    }

    @Test
    fun testForNotChangedOriginalWord() {
        val result = originalWordHandler.handle("cat", "cat")
        assertTrue(result is Result.OriginalWordNotChanged)
    }

    @Test
    fun testForDuplicateOriginalWord() {
        val result = originalWordHandler.handle("dog", "cat")
        assertTrue(result is Result.DuplicateOriginalWord)
    }

    @Test
    fun testForCorrectlyChangedOriginalWord() {
        val result = originalWordHandler.handle("tiger", "cat")
        assertTrue(result is Result.OriginalWordCorrectlyChanged)
    }
}