@file:Suppress("UNCHECKED_CAST")

package com.artyomefimov.pocketdictionary.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.viewmodel.word.WordViewModel
import com.artyomefimov.pocketdictionary.viewmodel.wordlist.WordListViewModel

class WordListViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WordListViewModel(repository) as T
}

class WordViewModelFactory(
    private val dictionaryRecord: DictionaryRecord?,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WordViewModel(dictionaryRecord, repository) as T
}

