package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord

/**
 * Handles view state and returns a new state according to a current state
 */
class ViewStateController {
    private var currentState: ViewState =
        ViewState.StableState

    fun setInitialViewState(viewState: ViewState) {
        currentState = viewState
    }

    fun getInitialViewState(dictionaryRecord: DictionaryRecord?): ViewState {
        dictionaryRecord?.let {
            return if (it.originalWord.isEmpty()) {
                currentState = ViewState.EditingState
                currentState
            } else {
                currentState
            }
        } ?: return currentState
    }

    fun getNewState(): ViewState {
        return if (currentState == ViewState.EditingState) {
            currentState = ViewState.StableState
            currentState
        } else {
            currentState = ViewState.EditingState
            currentState
        }
    }
}

enum class ViewState(
    val menuIcon: Int,
    val isEnabled: Boolean
) {
    EditingState(R.drawable.ic_action_edit_done, true),
    StableState(R.drawable.ic_action_edit, false)
}