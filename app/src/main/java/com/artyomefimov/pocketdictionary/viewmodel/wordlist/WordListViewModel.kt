package com.artyomefimov.pocketdictionary.viewmodel.wordlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.search.DictionarySearchUtil
import io.reactivex.disposables.Disposable

class WordListViewModel(
    private val repository: Repository,
    private val searchUtil: DictionarySearchUtil = DictionarySearchUtil()
) : ViewModel() {
    private companion object {
        const val TAG = "WordListViewModel"
    }

    private lateinit var subscription: Disposable

    var dictionary: List<DictionaryRecord> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadDictionary(
        onSuccessfulLoading: (List<DictionaryRecord>) -> Unit,
        onFailure: (message: Int) -> Unit
    ) {
        subscription = repository.getDictionary()
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
}