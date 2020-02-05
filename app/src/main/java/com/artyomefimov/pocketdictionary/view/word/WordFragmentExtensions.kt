package com.artyomefimov.pocketdictionary.view.word

import android.content.Context
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.artyomefimov.pocketdictionary.PocketDictionaryApplication
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.viewmodel.factory.WordViewModelFactory
import com.artyomefimov.pocketdictionary.viewmodel.word.WordViewModel
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.ViewState

internal fun WordFragment.initViewModel(bundleKey: String): WordViewModel {
    val dictionaryRecord = arguments?.getSerializable(bundleKey) as? DictionaryRecord
    val repository = PocketDictionaryApplication.repository(activity as Context)

    return ViewModelProvider(
        this,
        WordViewModelFactory(dictionaryRecord, repository)
    )[WordViewModel::class.java]
}

internal fun applyNewStateFor(
    viewState: ViewState,
    editItem: MenuItem?,
    originalWordText: EditText
) =
    viewState.apply {
        editItem?.setIcon(menuIcon)
        originalWordText.isEnabled = isEnabled
    }