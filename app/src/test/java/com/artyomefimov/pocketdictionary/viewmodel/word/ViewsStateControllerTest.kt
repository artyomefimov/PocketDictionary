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
            ViewStateController()

        assertEquals(ViewState.EDITING_STATE, controller.getInitialViewState(DictionaryRecord()))
    }

    @Test
    fun testGetStableStateInCaseOfNonEmptyOriginalWord() {
        val controller =
            ViewStateController()

        assertEquals(ViewState.STABLE_STATE, controller.getInitialViewState(DictionaryRecord("some word")))
    }

    @Test
    fun testGetDifferentViewStateEveryTime() {
        val controller =
            ViewStateController()

        val firstCallState = controller.getNewState()
        val secondCallState = controller.getNewState()

        assertEquals(ViewState.EDITING_STATE, firstCallState)
        assertEquals(ViewState.STABLE_STATE, secondCallState)
    }
}