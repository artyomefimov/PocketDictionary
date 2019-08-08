package com.artyomefimov.pocketdictionary.viewmodel.word

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class ViewsStateControllerTest {
    @Test
    fun testGetEditingStateInCaseOfEmptyOriginalWord() {
        val controller = ViewsStateController(DictionaryRecord())

        assertEquals(ViewState.EditingState, controller.getInitialViewState())
    }

    @Test
    fun testGetStableStateInCaseOfNonEmptyOriginalWord() {
        val controller = ViewsStateController(DictionaryRecord("some word"))

        assertEquals(ViewState.StableState, controller.getInitialViewState())
    }

    @Test
    fun testGetDifferentViewStateEveryTime() {
        val controller = ViewsStateController(DictionaryRecord())

        val firstCallState = controller.getNewState()
        val secondCallState = controller.getNewState()

        assertEquals(ViewState.EditingState, firstCallState)
        assertEquals(ViewState.StableState, secondCallState)
    }
}