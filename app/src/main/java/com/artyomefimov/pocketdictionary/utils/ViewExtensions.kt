package com.artyomefimov.pocketdictionary.utils

import android.content.ContextWrapper
import android.content.Intent
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.View
import com.artyomefimov.pocketdictionary.*
import com.artyomefimov.pocketdictionary.view.ConfirmDeletionDialog
import com.artyomefimov.pocketdictionary.view.MainActivity
import com.artyomefimov.pocketdictionary.view.word.EditTranslationDialog

fun View.getParentFragment(): Fragment? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is MainActivity) {
            return context.supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        context = context.baseContext
    }
    return null
}

fun DialogFragment.sendResult(resultCode: Int, intent: Intent) =
    targetFragment?.onActivityResult(
        targetRequestCode,
        resultCode,
        intent
    )

fun Fragment.showEditTranslationDialog(translation: String, position: Int) {
    if (fragmentManager != null) {
        val targetFragment = this
        val editTranslationDialog = EditTranslationDialog.newInstance(translation, position)
        editTranslationDialog.setTargetFragment(
            targetFragment,
            EDIT_TRANSLATION_DIALOG_REQUEST_CODE
        )
        editTranslationDialog.show(fragmentManager, EDIT_TRANSLATION_DIALOG_TAG)
    }
}

fun Fragment.showConfirmDeletionDialog(element: String) {
    if (fragmentManager != null) {
        val confirmDeletionDialog = ConfirmDeletionDialog.newInstance(element)
        confirmDeletionDialog.setTargetFragment(
            this,
            CONFIRM_DELETION_DIALOG_REQUEST_CODE
        )
        confirmDeletionDialog.show(fragmentManager, CONFIRM_DELETION_DIALOG_TAG)
    }
}