package com.artyomefimov.pocketdictionary.utils.view

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

inline fun <reified T : DialogFragment> Fragment.showDialog(stringValue: String, position: Int) {
    when (T::class) {
        EditTranslationDialog::class ->
            configureAndShow(
                EditTranslationDialog.newInstance(stringValue, position),
                EDIT_TRANSLATION_DIALOG_REQUEST_CODE,
                EDIT_TRANSLATION_DIALOG_TAG
            )
        ConfirmDeletionDialog::class ->
            configureAndShow(
                ConfirmDeletionDialog.newInstance(stringValue),
                CONFIRM_DELETION_DIALOG_REQUEST_CODE,
                CONFIRM_DELETION_DIALOG_TAG
            )
    }
}

fun Fragment.configureAndShow(dialog: DialogFragment, requestCode: Int, tag: String) {
    dialog.setTargetFragment(
        this,
        requestCode
    )
    dialog.show(fragmentManager, tag)
}
