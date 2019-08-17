package com.artyomefimov.pocketdictionary.view.word

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.view.MenuItem
import android.widget.EditText
import com.artyomefimov.pocketdictionary.PocketDictionaryApplication
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.viewmodel.factory.WordViewModelFactory
import com.artyomefimov.pocketdictionary.viewmodel.word.ViewState
import com.artyomefimov.pocketdictionary.viewmodel.word.WordViewModel

internal fun WordFragment.initViewModel(bundleKey: String): WordViewModel {
    val dictionaryRecord = arguments?.getSerializable(bundleKey) as DictionaryRecord
    val repository = PocketDictionaryApplication.repository(activity as Context)
    return ViewModelProviders.of(
        this,
        WordViewModelFactory(dictionaryRecord, repository)
    )[WordViewModel::class.java]
}

internal fun applyNewStateFor(viewState: ViewState, editItem: MenuItem, originalWordText: EditText) =
    viewState.apply {
        editItem.setIcon(menuIcon)
        originalWordText.isEnabled = isEnabled
    }