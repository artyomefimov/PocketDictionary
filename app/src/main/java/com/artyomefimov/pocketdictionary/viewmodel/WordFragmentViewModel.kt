package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.base.BaseViewModel
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WordFragmentViewModel(private val dictionaryRecord: DictionaryRecord) :
    BaseViewModel() { // todo implement vm for wordfragment
    @Inject
    lateinit var translateApi: TranslateApi
    @Inject
    lateinit var localStorage: LocalStorage

    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val originalWord: MutableLiveData<String> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    fun loadTranslation(originalWord: String, languagesPair: String) {
        subscription = translateApi.getTranslation(originalWord, languagesPair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .doOnTerminate { loadingVisibility.value = View.GONE }
            .subscribe(
                { response ->
                    loadingVisibility.value = View.GONE
                    dictionaryRecord.translations.add(response.responseData.translatedText)
                },
                {
                    loadingVisibility.value = View.GONE
                    errorMessage.value = R.string.request_error
                })

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    class Factory(private val dictionaryRecord: DictionaryRecord) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            WordFragmentViewModel(dictionaryRecord) as T
    }
}