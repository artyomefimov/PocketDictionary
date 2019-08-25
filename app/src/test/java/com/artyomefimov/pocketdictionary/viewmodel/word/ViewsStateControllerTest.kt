package com.artyomefimov.pocketdictionary.viewmodel.word

import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.ViewState
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.ViewStateController
import org.junit.Assert.assertEquals
import org.junit.Test

class ViewsStateControllerTest {
    @Test
    fun testGetEditingStateInCaseOfEmptyOriginalWord() {
        val controller =
            ViewStateController(DictionaryRecord())

        assertEquals(ViewState.EditingState, controller.getInitialViewState())
    }

    @Test
    fun testGetStableStateInCaseOfNonEmptyOriginalWord() {
        val controller =
            ViewStateController(DictionaryRecord("some word"))

        assertEquals(ViewState.StableState, controller.getInitialViewState())
    }

    @Test
    fun testGetDifferentViewStateEveryTime() {
        val controller =
            ViewStateController(DictionaryRecord())

        val firstCallState = controller.getNewState()
        val secondCallState = controller.getNewState()

        assertEquals(ViewState.EditingState, firstCallState)
        assertEquals(ViewState.StableState, secondCallState)
    }
}