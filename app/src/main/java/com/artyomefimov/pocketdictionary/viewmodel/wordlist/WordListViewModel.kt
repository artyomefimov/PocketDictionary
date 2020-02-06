package com.artyomefimov.pocketdictionary.viewmodel.wordlist

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.utils.search
import io.reactivex.disposables.Disposable

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

    fun loadDictionary(sharedPreferences: SharedPreferences?) {
        subscription = repository.getDictionary(sharedPreferences)
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .subscribe(
                { dictionaryRecords ->
                    loadingVisibility.value = View.GONE
                    dictionary = dictionaryRecords
                    dictionaryLiveData.value = dictionaryRecords
                },
                {
                    loadingVisibility.value = View.GONE
                    Log.e(TAG, "Exception during dictionary loading: ", it)
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
                    Log.e(TAG, "Exception during search: ", it)
                })
    }

    fun deleteDictionaryRecord(originalWord: String?, callUpdateService: () -> Unit) {
        val dictionary = getMutableListOf(dictionaryLiveData.value)
        dictionary.removeAll { it.originalWord == originalWord }
        dictionaryLiveData.value = dictionary

        repository.removeDictionaryRecord(originalWord)

        if (originalWord != null)
            callUpdateService()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}