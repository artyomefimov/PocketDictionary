package com.artyomefimov.pocketdictionary.viewmodel.word

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.*
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.exceptions.DuplicateTranslationException
import io.reactivex.disposables.Disposable

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord?,
    private val repository: Repository,
    private val viewStateController: ViewStateController = ViewStateController(),
    private val translationsHandler: TranslationsHandler = TranslationsHandler(),
    private val originalWordHandler: OriginalWordHandler = OriginalWordHandler(repository),
    var currentFavoriteTranslations: MutableList<String> = getMutableListOf(dictionaryRecord?.favoriteTranslations),
    val translationsLiveData: MutableLiveData<List<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData(),
    val toastMessageLiveData: MutableLiveData<Int> = MutableLiveData(),
    val snackbarMessageLiveData: MutableLiveData<Pair<Int, String>> = MutableLiveData()
) : ViewModel() {
    init {
        originalWordLiveData.value = dictionaryRecord?.originalWord
        translationsLiveData.value = dictionaryRecord?.translations
        loadingVisibility.value = View.GONE
    }

    private var subscription: Disposable? = null

    fun updateFavoriteTranslations(translation: String?) =
        updateFavoriteTranslation(currentFavoriteTranslations, translation)

    fun handleNewTranslationOnPosition(changedTranslation: String?, position: Int?) {
        try {
            translationsLiveData.value = translationsHandler.handleNewTranslationOnPosition(
                changedTranslation = changedTranslation,
                position = position,
                translationsLiveDataValue = translationsLiveData.value
            )
        } catch (e: DuplicateTranslationException) {
            toastMessageLiveData.value = R.string.duplicate_translation
        }
    }

    fun deleteTranslation(translation: String?) {
        translationsLiveData.value = translationsHandler.deleteTranslation(
            translation = translation,
            translationsLiveDataValue = translationsLiveData.value
        )
        currentFavoriteTranslations.remove(translation)
    }

    fun undoChanges(): ViewState {
        originalWordLiveData.value = dictionaryRecord?.originalWord
        return ViewState.StableState
    }

    fun setInitialViewState(viewState: ViewState) {
        viewStateController.setInitialViewState(viewState)
    }

    fun getInitialViewState(): ViewState =
        viewStateController.getInitialViewState(dictionaryRecord)


    fun getNewState(changedWord: String): ViewState {
        val newState = viewStateController.getNewState()
        return if (isOriginalWordUpdateWasFinished(newState))
            handleChangedOriginalWord(changedWord.trim())
        else
            newState
    }

    private fun isOriginalWordUpdateWasFinished(newState: ViewState): Boolean =
        newState == ViewState.StableState

    private fun handleChangedOriginalWord(changedWord: String): ViewState {
        when(val result = originalWordHandler.handle(changedWord, dictionaryRecord?.originalWord)) {
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
                            mutableTranslations = getMutableListOf(translationsLiveData.value)
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
            originalWordLiveData.value ?: dictionaryRecord?.originalWord ?: "",
            translationsLiveData.value ?: dictionaryRecord?.translations ?: ArrayList(),
            currentFavoriteTranslations
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