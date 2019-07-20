package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.search.DictionarySearchUtil
import org.junit.Assert.assertEquals

import org.junit.Test

class DictionarySearchUtilTest {

    private val word1 = DictionaryRecord("application")
    private val word2 = DictionaryRecord("applicable")
    private val word3 = DictionaryRecord("apply")
    private val word4 = DictionaryRecord("my application")
    private val dictionary = ArrayList<DictionaryRecord>().apply {
        add(word1)
        add(word2)
        add(word3)
        add(word4)
    }

    private val searchUtil = DictionarySearchUtil()

    @Test
    fun testCommonQueryReturnsAllWords() {
        assertEquals(dictionary, resultOfQuery("appl"))
    }

    @Test
    fun testExactQueryReturnsExactResult() {
        assertEquals(word3, resultOfQuery("apply")[0])
    }

    @Test
    fun testNullableQueryReturnsEmptyDictionary() {
        assertEquals(0, resultOfQuery(null).size)
    }

    @Test
    fun testQueryWithIncorrectSymbolsCanNotFindWords() {
        assertEquals(0, resultOfQuery("apply01@").size)
    }

    @Test
    fun testWordsThatContainSymbolsFromQueryNotFromStartAreNotIncludedInResult() {
        assertEquals(dictionary, resultOfQuery("ppl"))
    }

    @Test
    fun testUpperCaseQueryReturnsSameResultAsLowerCase() {
        assertEquals(resultOfQuery("APPL"), resultOfQuery("appl"))
    }

    @Test
    fun testQueryWithSpace() {
        assertEquals(word4, resultOfQuery("my")[0])
    }

    private fun resultOfQuery(query: String?): List<DictionaryRecord> {
        return searchUtil.search(query, dictionary)
    }
}