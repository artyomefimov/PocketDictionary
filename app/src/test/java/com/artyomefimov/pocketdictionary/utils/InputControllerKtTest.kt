package com.artyomefimov.pocketdictionary.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputControllerKtTest {
    @Test
    fun testIfInputWithSpaceIsCorrect() {
        assertTrue(isLatinInputCorrect("some string"))
    }

    @Test
    fun testIfUpperCaseInputIsCorrect() {
        assertTrue(isLatinInputCorrect("SOME STRING"))
    }

    @Test
    fun testIfMultipleCaseInputIsCorrect() {
        assertTrue(isLatinInputCorrect("SoMe Sttr iNG"))
    }

    @Test
    fun testIfInputWithNumbersIsIncorrect() {
        assertFalse(isLatinInputCorrect("string2"))
    }

    @Test
    fun testIfInputWithSpecialSymbolsIsIncorrect() {
        assertFalse(isLatinInputCorrect("{some string $;%2@}"))
    }

    @Test
    fun testIfBlankInputIsIncorrect() {
        assertFalse(isLatinInputCorrect("       "))
    }

    @Test
    fun testIfEmptyInputIsIncorrect() {
        assertFalse(isLatinInputCorrect(""))
    }
}