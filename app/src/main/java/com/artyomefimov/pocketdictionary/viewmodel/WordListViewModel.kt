package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.di.*
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.artyomefimov.pocketdictionary.utils.DictionarySearchUtil
import com.artyomefimov.pocketdictionary.view.wordlistfragment.WordListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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

    var dictionary: List<DictionaryRecord> = ArrayList()

    private lateinit var subscription: Disposable
    private val searchUtil = DictionarySearchUtil()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadDictionary(onSuccessfulLoading: (List<DictionaryRecord>) -> Unit, onFailure: (message: Int) -> Unit) {
        subscription = localStorage.loadDictionary()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .doOnTerminate { loadingVisibility.value = View.GONE }
            .subscribe(
                { dictionaryRecords ->
                    loadingVisibility.value = View.GONE
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