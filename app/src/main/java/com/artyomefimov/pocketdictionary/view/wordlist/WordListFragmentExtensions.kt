@file:Suppress("UNCHECKED_CAST")

package com.artyomefimov.pocketdictionary.view.wordlist

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.artyomefimov.pocketdictionary.PERMISSIONS_REQUEST_CODE
import com.artyomefimov.pocketdictionary.PocketDictionaryApplication
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.view.adapters.WordListAdapter
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.view.longToast
import com.artyomefimov.pocketdictionary.view.MainActivity
import com.artyomefimov.pocketdictionary.view.needed_permissions
import com.artyomefimov.pocketdictionary.view.word.WordFragment
import com.artyomefimov.pocketdictionary.viewmodel.factory.WordListViewModelFactory
import com.artyomefimov.pocketdictionary.viewmodel.wordlist.WordListViewModel
import kotlinx.android.synthetic.main.fragment_list_words.*

internal fun WordListFragment.initViewModel(): WordListViewModel {
    val repository = PocketDictionaryApplication.repository(activity as Context)
    return ViewModelProviders.of(
        this,
        WordListViewModelFactory(repository)
    )[WordListViewModel::class.java]
}

internal fun WordListFragment.openWordFragmentFor(dictionaryRecord: DictionaryRecord?) {
    if (this.activity != null) {
        val mainActivity = this.activity as MainActivity
        mainActivity.replaceFragment(WordFragment.newInstance(dictionaryRecord))
    }
}

internal fun WordListFragment.showSearchResults(searchResult: List<DictionaryRecord>) {
    (recycler_view_word_list.adapter as WordListAdapter<DictionaryRecord>)
        .updateData(searchResult)
}

internal fun WordListFragment.loadDictionary() {
    viewModel.loadDictionary()
}

internal fun WordListFragment.showDictionary(dictionary: List<DictionaryRecord>?) {
    (recycler_view_word_list.adapter as WordListAdapter<DictionaryRecord>)
        .updateData(dictionary)
}

internal fun WordListFragment.requestPermissions() {
    requestPermissions(needed_permissions, PERMISSIONS_REQUEST_CODE)
}

internal fun WordListFragment.showPermissionsMessage() =
    longToast(R.string.give_needed_permissions)