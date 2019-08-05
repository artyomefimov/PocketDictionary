package com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.di.viewmodel.DaggerViewModelComponent
import com.artyomefimov.pocketdictionary.di.viewmodel.NetworkModule
import com.artyomefimov.pocketdictionary.di.viewmodel.ViewModelComponent
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord,
    private val localStorage: LocalStorage,
    private val viewsStateController: ViewsStateController = ViewsStateController(dictionaryRecord),
    val translationsLiveData: MutableLiveData<List<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
) : ViewModel() {
    private companion object {
        const val TAG = "WordViewModel"
    }

    private val component: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        component.inject(this)
        originalWordLiveData.value = dictionaryRecord.originalWord
        translationsLiveData.value = dictionaryRecord.translations
        loadingVisibility.value = View.GONE
    }

    @Inject
    lateinit var translateApi: TranslateApi
    private var subscription: Disposable? = null
    val messageLiveData: MutableLiveData<Int> = MutableLiveData()

    fun changeTranslation(changedTranslation: String?, position: Int?) {
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

    fun addTranslation(translation: String) {
        val newTranslations = getMutableListOf(translationsLiveData.value!!)
        newTranslations.add(translation)
        translationsLiveData.value = newTranslations
    } // todo change translation on special position, not create new one

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
            handleChangedOriginalWord(changedWord)
        else
            newState
    }

    private fun isOriginalWordUpdateWasFinished(newState: ViewState): Boolean =
        newState == ViewState.StableState

    private fun handleChangedOriginalWord(changedWord: String): ViewState {
        if (changedWord.isEmpty()) {
            messageLiveData.value = R.string.empty_original_word
            return ViewState.EditingState
        }
        if (isWordNotChanged(changedWord))
            return ViewState.StableState

        return if (isDuplicate(changedWord)) {
            messageLiveData.value = R.string.duplicate_original_word

            val previousWord = originalWordLiveData.value
            originalWordLiveData.value = previousWord

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
        localStorage.getDictionaryRecord(word)
            .originalWord.isNotEmpty()

    private fun loadOriginalWordTranslation(originalWord: String, languagesPair: LanguagePairs) {
        subscription = translateApi.getTranslation(originalWord, languagesPair.pair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    fun updateStorage(callUpdateService: (oldRecord: DictionaryRecord, updatedRecord: DictionaryRecord) -> Unit) {
        val updatedDictionaryRecord = DictionaryRecord(
            originalWordLiveData.value!!,
            translationsLiveData.value!!
        )
        callUpdateService(dictionaryRecord, updatedDictionaryRecord)
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    class Factory(
        private val dictionaryRecord: DictionaryRecord,
        private val localStorage: LocalStorage
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            WordViewModel(dictionaryRecord, localStorage) as T
    }
}