package com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel

import com.artyomefimov.pocketdictionary.model.DictionaryRecord

class ViewsStateController(
    private val dictionaryRecord: DictionaryRecord
) {

    private var currentState: ViewState = ViewState.StableState

    fun getInitialViewState(): ViewState {
        return if (dictionaryRecord.originalWord.isEmpty()) {
            currentState = ViewState.EditingState
            currentState
        } else {
            currentState = ViewState.StableState
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