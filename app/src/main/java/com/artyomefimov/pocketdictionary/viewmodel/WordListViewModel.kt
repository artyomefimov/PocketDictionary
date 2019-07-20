package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.di.DaggerViewModelComponent
import com.artyomefimov.pocketdictionary.di.StorageModule
import com.artyomefimov.pocketdictionary.di.ViewModelComponent
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.DictionarySearchUtil
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class WordListViewModel : ViewModel() {
    private val componentWordList: ViewModelComponent = DaggerViewModelComponent
        .builder()
        .storageModule(StorageModule)
        .build()

    init {
        componentWordList.inject(this)
    }

    @Inject
    lateinit var localStorage: LocalStorage

    private lateinit var subscription: Disposable
    private val searchUtil = DictionarySearchUtil()

    var dictionary: List<DictionaryRecord> = ArrayList()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadDictionary(
        onSuccessfulLoading: (List<DictionaryRecord>) -> Unit,
        onFailure: (message: Int) -> Unit) {

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
                    onFailure(R.string.request_error)
                })
    }

    fun findRecords(query: String?): List<DictionaryRecord> {
        return searchUtil.search(query, dictionary)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}