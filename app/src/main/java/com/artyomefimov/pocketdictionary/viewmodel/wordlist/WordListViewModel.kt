package com.artyomefimov.pocketdictionary.viewmodel.wordlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.utils.search
import io.reactivex.disposables.Disposable
import java.io.EOFException

class WordListViewModel(
    private val repository: Repository,
    var dictionary: List<DictionaryRecord> = ArrayList(),
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData(),
    val messageLiveData: MutableLiveData<Int> = MutableLiveData(),
    val dictionaryLiveData: MutableLiveData<List<DictionaryRecord>> = MutableLiveData()
) : ViewModel() {
    private companion object {
        const val TAG = "WordListViewModel"
    }

    private lateinit var subscription: Disposable

    fun loadDictionary() {
        subscription = repository.getDictionary()
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .subscribe(
                { dictionaryRecords ->
                    loadingVisibility.value = View.GONE
                    dictionary = dictionaryRecords
                    dictionaryLiveData.value = dictionaryRecords
                },
                {
                    loadingVisibility.value = View.GONE
                    Log.e(TAG, "exception during dictionary loading", it)
                    if (it !is EOFException)
                        messageLiveData.value = R.string.local_loading_error
                })
    }

    fun findRecords(
        query: String?,
        onSuccessfulLoading: (List<DictionaryRecord>) -> Unit
    ) {
        subscription = search(query, dictionary)
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .subscribe(
                {
                    loadingVisibility.value = View.GONE
                    onSuccessfulLoading(it)
                },
                {
                    loadingVisibility.value = View.GONE
                    Log.e(TAG, "Exception occurred during search", it)
                })
    }

    fun deleteDictionaryRecord(originalWord: String, callUpdateService: () -> Unit) {
        val dictionary = getMutableListOf(dictionaryLiveData.value!!)
        dictionary.removeAll { it.originalWord == originalWord }
        dictionaryLiveData.value = dictionary

        repository.removeDictionaryRecord(originalWord)
        callUpdateService()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}