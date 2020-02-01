package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.isLatinInputIncorrect

/**
 * Handles changes of the original word by comparing its old and new versions
 */
class OriginalWordHandler(private val repository: Repository) {
    fun handle(changedWord: String, originalWord: String?): Result {
        return when {
            isLatinInputIncorrect(changedWord) ->
                Result.LatinInputIncorrect(R.string.incorrect_original_word, ViewState.EditingState)
            isWordNotChanged(changedWord, originalWord) ->
                Result.OriginalWordNotChanged(ViewState.StableState)
            isDuplicate(changedWord) ->
                Result.DuplicateOriginalWord(R.string.duplicate_original_word, ViewState.EditingState)
            else ->
                Result.OriginalWordCorrectlyChanged(R.string.is_api_request_needed, changedWord, ViewState.StableState)
        }
    }

    private fun isWordNotChanged(changedWord: String, originalWord: String?): Boolean =
        changedWord == originalWord

    private fun isDuplicate(changedWord: String): Boolean =
        repository
            .getDictionaryRecord(changedWord)?.originalWord?.isNotEmpty() ?: false
}

sealed class Result {
    data class LatinInputIncorrect(val messageResId: Int, val viewState: ViewState): Result()
    data class OriginalWordNotChanged(val viewState: ViewState): Result()
    data class DuplicateOriginalWord(val messageResId: Int, val viewState: ViewState): Result()
    data class OriginalWordCorrectlyChanged(val snackbarMessageResId: Int, val changedWord: String, val viewState: ViewState): Result()
}