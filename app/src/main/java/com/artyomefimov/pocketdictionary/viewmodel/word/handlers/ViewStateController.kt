package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord

class ViewStateController(
    private val dictionaryRecord: DictionaryRecord
) {

    private var currentState: ViewState =
        ViewState.StableState

    fun setInitialViewState(viewState: ViewState) {
        currentState = viewState
    }

    fun getInitialViewState(): ViewState {
        return if (dictionaryRecord.originalWord.isEmpty()) {
            currentState = ViewState.EditingState
            currentState
        } else {
            currentState
        }
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