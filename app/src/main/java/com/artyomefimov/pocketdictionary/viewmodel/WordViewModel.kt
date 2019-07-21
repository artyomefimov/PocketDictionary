package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.di.DaggerViewModelComponent
import com.artyomefimov.pocketdictionary.di.NetworkModule
import com.artyomefimov.pocketdictionary.di.StorageModule
import com.artyomefimov.pocketdictionary.di.ViewModelComponent
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordViewModel(
    private val dictionaryRecord: DictionaryRecord,
    val translationsLiveData: MutableLiveData<MutableList<String>> = MutableLiveData(),
    val originalWordLiveData: MutableLiveData<String> = MutableLiveData(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
) : ViewModel() {
    companion object {const val TAG = "WordViewModel"}

    private val component: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .networkModule(NetworkModule)
        .storageModule(StorageModule)
        .build()

    init {
        component.inject(this)
        translationsLiveData.value = dictionaryRecord.translations
        originalWordLiveData.value = dictionaryRecord.originalWord
        loadingVisibility.value = View.GONE
    }

    private var subscription: Disposable? = null

    @Inject
    lateinit var translateApi: TranslateApi
    @Inject
    lateinit var localStorage: LocalStorage

    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    fun loadOriginalWordTranslation(languagesPair: LanguagePairs) {
        if (dictionaryRecord.isNotApiRequestWasPerformed) {
            subscription = translateApi.getTranslation(dictionaryRecord.originalWord, languagesPair.pair)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
                .subscribe(
                    { response ->
                        loadingVisibility.value = View.GONE
                        translationsLiveData.value?.add(response.responseData.translatedText)
                    },
                    {
                        loadingVisibility.value = View.GONE
                        errorMessage.value = R.string.request_error
                    })
            dictionaryRecord.isNotApiRequestWasPerformed = true
        }

    }

    fun changeTranslation(changedTranslation: String?, position: Int?) {
        if (isReceivedDataValid(changedTranslation, position)) {
            val newTranslations = translationsLiveData.value
            newTranslations?.set(position!!, changedTranslation!!)
            translationsLiveData.value = newTranslations
        } else {
            Log.d(TAG, "Invalid translation $changedTranslation and position $position ")
        }
    }

    private fun isReceivedDataValid(changedTranslation: String?, position: Int?) =
        changedTranslation != null && position != null && position != -1

    fun addEmptyTranslation() {
        val newTranslations = translationsLiveData.value
        newTranslations?.add("")
        translationsLiveData.value = newTranslations
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    class Factory(private val dictionaryRecord: DictionaryRecord) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            WordViewModel(dictionaryRecord) as T
    }
}