package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord

/**
 * Handles view state and returns a new state according to a current state
 */
class ViewStateController {
    private var currentState: ViewState =
        ViewState.STABLE_STATE

    fun setInitialViewState(viewState: ViewState) {
        currentState = viewState
    }

    fun getInitialViewState(dictionaryRecord: DictionaryRecord?): ViewState {
        dictionaryRecord?.let {
            return if (it.originalWord.isEmpty()) {
                currentState = ViewState.EDITING_STATE
                currentState
            } else {
                currentState
            }
        } ?: return currentState
    }

    fun getNewState(): ViewState {
        return if (currentState == ViewState.EDITING_STATE) {
            currentState = ViewState.STABLE_STATE
            currentState
        } else {
            currentState = ViewState.EDITING_STATE
            currentState
        }
    }
}

enum class ViewState(
    val menuIcon: Int,
    val isEnabled: Boolean
) {
    EDITING_STATE(R.drawable.ic_action_edit_done, true),
    STABLE_STATE(R.drawable.ic_action_edit, false)
}