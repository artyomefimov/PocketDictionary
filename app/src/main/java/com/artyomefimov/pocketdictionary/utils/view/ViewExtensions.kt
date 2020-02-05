package com.artyomefimov.pocketdictionary.utils.view

import android.content.Intent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.artyomefimov.pocketdictionary.CONFIRM_DELETION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.CONFIRM_DELETION_DIALOG_TAG
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_TAG
import com.artyomefimov.pocketdictionary.view.dialogs.ConfirmDeletionDialog
import com.artyomefimov.pocketdictionary.view.dialogs.EditTranslationDialog

fun DialogFragment.sendResult(resultCode: Int, intent: Intent) =
    targetFragment?.onActivityResult(
        targetRequestCode,
        resultCode,
        intent
    )

inline fun <reified T : DialogFragment> Fragment.showDialog(stringValue: String?, position: Int) {
    when (T::class) {
        EditTranslationDialog::class ->
            configureAndShowDialog(
                EditTranslationDialog.newInstance(stringValue, position),
                EDIT_TRANSLATION_DIALOG_REQUEST_CODE,
                EDIT_TRANSLATION_DIALOG_TAG
            )
        ConfirmDeletionDialog::class ->
            configureAndShowDialog(
                ConfirmDeletionDialog.newInstance(stringValue),
                CONFIRM_DELETION_DIALOG_REQUEST_CODE,
                CONFIRM_DELETION_DIALOG_TAG
            )
    }
}

fun Fragment.configureAndShowDialog(dialog: DialogFragment, requestCode: Int, tag: String) {
    dialog.setTargetFragment(
        this,
        requestCode
    )
    dialog.show(parentFragmentManager, tag)
}
