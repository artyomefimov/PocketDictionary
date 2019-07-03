package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.base.BaseViewModel
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.model.Translation
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TranslationViewModel : BaseViewModel() { // todo implement vm for wordfragment
    @Inject
    lateinit var translateApi: TranslateApi
    @Inject
    lateinit var localStorage: LocalStorage
    private lateinit var subscription: Disposable
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadTranslation(originalWord: String, languagesPair: String) {
        subscription = translateApi.getTranslation(originalWord, languagesPair)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onOperationStart() }
            .doOnTerminate { onOperationEnd() }
            .subscribe(
                { result -> onOperationSuccess(result) },
                { e -> onOperationUnsuccess(e) })

    }

//    fun writeTranslations() {
//        val traslation = Translation("word")
//        traslation.translations?.add("1")
//        traslation.translations?.add("2")
//        traslation.translations?.add("3")
//        localStorage.putNewTranslations(traslation)
//    }
//
//    fun getTrans(originalWord: String): Translation {
//        return localStorage.getTranslations(originalWord)
//    }

    fun onOperationStart() {
        loadingVisibility.value = View.VISIBLE
    }

    fun onOperationEnd() {
        loadingVisibility.value = View.GONE
    }

    fun onOperationSuccess(response: Response) {
        Log.d("TranslationViewModel", response.responseData.translatedText)
    }

    fun onOperationUnsuccess(error: Throwable) {
        Log.d("TranslationViewModel", error.message)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}