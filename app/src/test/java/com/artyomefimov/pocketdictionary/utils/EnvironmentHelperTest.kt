package com.artyomefimov.pocketdictionary.utils

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class EnvironmentHelperTest {
    @Mock
    private lateinit var environmentHelper: EnvironmentHelper

    @Test
    fun testEnvironmentHelper() {
        doReturn("path").whenever(environmentHelper).getExternalStorageDirectory()
        assertEquals("path", environmentHelper.getExternalStorageDirectory())
    }
}