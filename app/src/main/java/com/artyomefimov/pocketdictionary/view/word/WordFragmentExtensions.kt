package com.artyomefimov.pocketdictionary.view.word

import android.arch.lifecycle.ViewModelProviders
import android.view.MenuItem
import android.widget.EditText
import com.artyomefimov.pocketdictionary.BaseApp
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_TAG
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel.ViewState
import com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_word.*

internal fun WordFragment.showEditTranslationDialog(translation: String, position: Int) {
    if (fragmentManager != null) {
        val editTranslationDialog = EditTranslationDialog.newInstance(translation, position)
        editTranslationDialog.setTargetFragment(this, EDIT_TRANSLATION_DIALOG_REQUEST_CODE)
        editTranslationDialog.show(fragmentManager, EDIT_TRANSLATION_DIALOG_TAG)
    }
}

internal fun WordFragment.initViewModel(bundleKey: String): WordViewModel {
    val dictionaryRecord = arguments?.getSerializable(bundleKey) as DictionaryRecord
    val localStorage = (activity?.application as BaseApp).localStorage
    return ViewModelProviders.of(
        this,
        WordViewModel.Factory(dictionaryRecord, localStorage)
    )[WordViewModel::class.java]
}

internal fun applyNewStateFor(viewState: ViewState, editItem: MenuItem, originalWordText: EditText) =
    viewState.apply {
        editItem.setIcon(menuIcon)
        originalWordText.isEnabled = isEnabled
    }