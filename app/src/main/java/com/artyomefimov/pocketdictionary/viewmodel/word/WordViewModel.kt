package com.artyomefimov.pocketdictionary.viewmodel.word

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.utils.isLatinInputIncorrect
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.exceptions.DuplicateTranslationException
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.TranslationsHandler
import io.reactivex.disposables.Disposable

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord,
    private val repository: Repository,
    private val viewsStateController: ViewsStateController = ViewsStateController(dictionaryRecord),
    private val translationsHandler: TranslationsHandler = TranslationsHandler(),
    val translationsLiveData: MutableLiveData<List<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData(),
    val messageLiveData: MutableLiveData<Int> = MutableLiveData()
) : ViewModel() {
    init {
        originalWordLiveData.value = dictionaryRecord.originalWord
        translationsLiveData.value = dictionaryRecord.translations
        loadingVisibility.value = View.GONE
    }

    private var subscription: Disposable? = null

    fun handleNewTranslationOnPosition(changedTranslation: String?, position: Int?) {
        try {
            translationsLiveData.value = translationsHandler.handleNewTranslationOnPosition(
                changedTranslation = changedTranslation,
                position = position,
                translationsLiveDataValue = translationsLiveData.value!!
            )
        } catch (e: DuplicateTranslationException) {
            messageLiveData.value = R.string.duplicate_translation
        }
    }

    fun deleteTranslation(translation: String) {
        translationsLiveData.value = translationsHandler.deleteTranslation(
            translation = translation,
            translationsLiveDataValue = translationsLiveData.value!!
        )
    }

    fun undoChanges(): ViewState {
        originalWordLiveData.value = dictionaryRecord.originalWord
        return ViewState.StableState
    }

    fun setInitialViewState(viewState: ViewState) {
        viewsStateController.setInitialViewState(viewState)
    }

    fun getInitialViewState(): ViewState =
        viewsStateController.getInitialViewState()


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
                    try {
                        translationsLiveData.value = translationsHandler.addTranslation(
                            translation = response.responseData.translatedText,
                            mutableTranslations = getMutableListOf(translationsLiveData.value!!)
                        )
                    } catch (e: DuplicateTranslationException) { }
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