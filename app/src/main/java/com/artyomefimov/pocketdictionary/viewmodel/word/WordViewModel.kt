package com.artyomefimov.pocketdictionary.viewmodel.word

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.OriginalWordHandler
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.Result
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.exceptions.DuplicateTranslationException
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.TranslationsHandler
import io.reactivex.disposables.Disposable

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord,
    private val repository: Repository,
    private val viewsStateController: ViewsStateController = ViewsStateController(dictionaryRecord),
    private val translationsHandler: TranslationsHandler = TranslationsHandler(),
    private val originalWordHandler: OriginalWordHandler = OriginalWordHandler(repository),
    val translationsLiveData: MutableLiveData<List<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData(),
    val toastMessageLiveData: MutableLiveData<Int> = MutableLiveData(),
    val snackbarMessageLiveData: MutableLiveData<Pair<Int, String>> = MutableLiveData()
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
            toastMessageLiveData.value = R.string.duplicate_translation
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
        when(val result = originalWordHandler.handle(changedWord, originalWordLiveData.value!!)) {
            is Result.LatinInputIncorrect -> {
                toastMessageLiveData.value = result.messageResId
                return result.viewState
            }
            is Result.OriginalWordNotChanged ->
                return result.viewState
            is Result.DuplicateOriginalWord -> {
                toastMessageLiveData.value = result.messageResId
                revertOriginalWord()

                return result.viewState
            }
            is Result.OriginalWordCorrectlyChanged -> {
                originalWordLiveData.value = result.changedWord
                snackbarMessageLiveData.value = result.snackbarMessageResId to result.changedWord

                return result.viewState
            }
        }
    }

    private fun revertOriginalWord() {
        val previousWord = originalWordLiveData.value
        originalWordLiveData.value = previousWord
    }

    fun loadOriginalWordTranslation(originalWord: String, languagesPair: LanguagePairs) {
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
                    toastMessageLiveData.value = R.string.api_request_error
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