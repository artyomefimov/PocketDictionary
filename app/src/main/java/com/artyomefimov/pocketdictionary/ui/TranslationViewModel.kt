package com.artyomefimov.pocketdictionary.ui

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.base.BaseViewModel
import com.artyomefimov.pocketdictionary.model.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TranslationViewModel : BaseViewModel() {
    @Inject
    lateinit var translateApi: TranslateApi
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