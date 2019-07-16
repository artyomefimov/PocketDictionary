package com.artyomefimov.pocketdictionary.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.di.*
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.storage.LocalStorage
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
    lateinit var adapter: WordListAdapter
    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    fun loadDictionary() {
        localStorage.readData()
        subscription = localStorage.loadDictionary()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingVisibility.value = View.VISIBLE }
            .doOnTerminate { loadingVisibility.value = View.GONE }
            .subscribe(
                { dictionaryRecords ->
                    loadingVisibility.value = View.GONE
                    adapter.dictionaryRecords = dictionaryRecords
                    adapter.notifyDataSetChanged()
                },
                {
                    loadingVisibility.value = View.GONE
                    errorMessage.value = R.string.request_error
                })
    }

    fun addWord() {
        localStorage.addDictionaryRecord(DictionaryRecord(("test word"), ArrayList<String>().apply { add("1") }))
        localStorage.writeData()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}