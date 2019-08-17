package com.artyomefimov.pocketdictionary.viewmodel.word

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.NEW_TRANSLATION_POSITION
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.utils.isLatinInputIncorrect
import io.reactivex.disposables.Disposable

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord,
    private val repository: Repository,
    private val viewsStateController: ViewsStateController = ViewsStateController(dictionaryRecord),
    val translationsLiveData: MutableLiveData<List<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData(),
    val messageLiveData: MutableLiveData<Int> = MutableLiveData()
) : ViewModel() {
    private companion object {
        const val TAG = "WordViewModel"
    }

    init {
        originalWordLiveData.value = dictionaryRecord.originalWord
        translationsLiveData.value = dictionaryRecord.translations
        loadingVisibility.value = View.GONE
    }

    private var subscription: Disposable? = null

    fun deleteTranslation(translation: String) {
        val newTranslations = getMutableListOf(translationsLiveData.value!!)
        newTranslations.remove(translation)
        translationsLiveData.value = newTranslations
    }

    fun handleNewTranslationOnPosition(translation: String?, position: Int?) {
        if (NEW_TRANSLATION_POSITION == position)
            addTranslation(translation!!)
        else
            changeTranslation(translation, position)
    }

    private fun changeTranslation(changedTranslation: String?, position: Int?) {
        if (isReceivedDataValid(changedTranslation, position)) {
            val newTranslations = getMutableListOf(translationsLiveData.value!!)
            newTranslations[position!!] = changedTranslation!!
            translationsLiveData.value = newTranslations
        } else {
            Log.d(TAG, "Invalid translation $changedTranslation and position $position")
        }
    }

    private fun isReceivedDataValid(changedTranslation: String?, position: Int?) =
        changedTranslation != null && position != null && position != -1

    private fun addTranslation(translation: String) {
        val newTranslations = getMutableListOf(translationsLiveData.value!!)
        newTranslations.add(translation)
        translationsLiveData.value = newTranslations
    }

    fun undoChanges(): ViewState {
        originalWordLiveData.value = dictionaryRecord.originalWord
        return ViewState.StableState
    }

    fun getInitialViewState(): ViewState {
        return viewsStateController.getInitialViewState()
    }

    fun getNewState(changedWord: String): ViewState {
        val newState = viewsStateController.getNewState()
        return if (isOriginalWordUpdateWasFinished(newState))
            handleChangedOriginalWord(changedWord.trim())
        else
            newState
    }

    private fun isOriginalWordUpdateWasFinished(newState: ViewState): Boolean =
        newState == ViewState.StableState

    private fun handleChangedOriginalWord(changedWord: String): ViewState {
        if (isLatinInputIncorrect(changedWord)) {
            messageLiveData.value = R.string.incorrect_original_word
            return ViewState.EditingState
        }
        if (isWordNotChanged(changedWord))
            return ViewState.StableState

        return if (isDuplicate(changedWord)) {
            messageLiveData.value = R.string.duplicate_original_word
            revertOriginalWord()

            ViewState.EditingState
        } else {
            originalWordLiveData.value = changedWord
            loadOriginalWordTranslation(changedWord, LanguagePairs.FromEnglishToRussian)

            ViewState.StableState
        }
    }

    private fun isWordNotChanged(word: String): Boolean =
        word == dictionaryRecord.originalWord

    private fun isDuplicate(word: String): Boolean =
        repository.getDictionaryRecord(word)
            .originalWord.isNotEmpty()

    private fun revertOriginalWord() {
        val previousWord = originalWordLiveData.value
        originalWordLiveData.value = previousWord
    }

    private fun loadOriginalWordTranslation(originalWord: String, languagesPair: LanguagePairs) {
        subscription = repository.getTranslation(originalWord, languagesPair)
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .subscribe(
                { response ->
                    loadingVisibility.value = View.GONE
                    addTranslation(response.responseData.translatedText)
                    messageLiveData.value = R.string.manual_adding_translations_proposal
                },
                {
                    loadingVisibility.value = View.GONE
                    messageLiveData.value = R.string.api_request_error
                })
    }

    fun updateDictionary(callUpdateService: () -> Unit) {
        val updatedDictionaryRecord = DictionaryRecord(
            originalWordLiveData.value!!,
            translationsLiveData.value!!
        )
        val isUpdated = repository.updateDictionaryRecord(dictionaryRecord, updatedDictionaryRecord)
        if (isUpdated)
            callUpdateService()
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }
}