package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.search.DictionarySearchUtil
import io.reactivex.disposables.Disposable

class WordListViewModel(private val localStorage: LocalStorage) : ViewModel() {
    private companion object {
        const val TAG = "WordListViewModel"
    }

    private lateinit var subscription: Disposable
    private val searchUtil = DictionarySearchUtil()

    var dictionary: List<DictionaryRecord> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadDictionary(
        onSuccessfulLoading: (List<DictionaryRecord>) -> Unit,
        onFailure: (message: Int) -> Unit
    ) {
        subscription = localStorage.loadDictionary()
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .subscribe(
                { dictionaryRecords ->
                    loadingVisibility.value = View.GONE
                    dictionary = dictionaryRecords
                    onSuccessfulLoading(dictionaryRecords)
                },
                {
                    loadingVisibility.value = View.GONE
                    Log.e(TAG, "exception during dictionary loading", it)
                    onFailure(R.string.local_loading_error)
                })
    }

    fun findRecords(query: String?): List<DictionaryRecord> {
        return searchUtil.search(query, dictionary) // todo maybe do in background
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    class Factory(private val localStorage: LocalStorage) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            WordListViewModel(localStorage) as T
    }
}