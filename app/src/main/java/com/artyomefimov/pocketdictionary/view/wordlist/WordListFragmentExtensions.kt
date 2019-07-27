package com.artyomefimov.pocketdictionary.view.wordlist

import android.widget.Toast
import com.artyomefimov.pocketdictionary.PERMISSIONS_REQUEST_CODE
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.view.MainActivity
import com.artyomefimov.pocketdictionary.view.needed_permissions
import com.artyomefimov.pocketdictionary.view.word.WordFragment
import kotlinx.android.synthetic.main.fragment_list_words.*

internal fun WordListFragment.openWordFragmentFor(dictionaryRecord: DictionaryRecord) {
    if (this.activity != null) {
        val mainActivity = this.activity as MainActivity
        mainActivity.replaceFragment(WordFragment.newInstance(dictionaryRecord))
    }
}

internal fun WordListFragment.showSearchResults(result: List<DictionaryRecord>) {
    (recycler_view_word_list.adapter as WordListAdapter)
        .updateDictionary(result)
}

internal fun WordListFragment.showDictionary(dictionary: List<DictionaryRecord>) {
    (recycler_view_word_list.adapter as WordListAdapter)
        .updateDictionary(dictionary)
}

internal fun WordListFragment.loadDictionary() {
    viewModel.loadDictionary(
        onSuccessfulLoading = { receivedDictionary -> showDictionary(receivedDictionary) },
        onFailure = { errorMessage ->
            Toast.makeText(
                this.activity,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        })
}

internal fun WordListFragment.requestPermissions() {
    requestPermissions(needed_permissions, PERMISSIONS_REQUEST_CODE)
}

internal fun WordListFragment.showPermissionsMessage() {
    Toast.makeText(
        this.activity,
        R.string.give_needed_permissions,
        Toast.LENGTH_LONG
    ).show()
}