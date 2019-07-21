package com.artyomefimov.pocketdictionary.view.word

import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_TAG

fun WordFragment.showEditTranslationDialog(translation: String, position: Int) {
    if (fragmentManager != null) {
        val editTranslationDialog = EditTranslationDialog.newInstance(translation, position)
        editTranslationDialog.setTargetFragment(this, EDIT_TRANSLATION_DIALOG_REQUEST_CODE)
        editTranslationDialog.show(fragmentManager, EDIT_TRANSLATION_DIALOG_TAG)
    }
}