package com.artyomefimov.pocketdictionary.utils

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import org.junit.Assert.assertEquals
import org.junit.Before

import org.junit.Test

class DictionarySearchUtilTest {

    private val word1 = DictionaryRecord("application")
    private val word2 = DictionaryRecord("applicable")
    private val word3 = DictionaryRecord("apply")
    private val dictionary = ArrayList<DictionaryRecord>().apply {
        add(word1)
        add(word2)
        add(word3)
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
        assertEquals(0, resultOfQuery("ppl").size)
    }

    @Test
    fun testUpperCaseQueryReturnsSameResultAsLowerCase() {
        assertEquals(resultOfQuery("APPL"), resultOfQuery("appl"))
    }

    private fun resultOfQuery(query: String?): List<DictionaryRecord> {
        return searchUtil.search(query, dictionary)
    }
}