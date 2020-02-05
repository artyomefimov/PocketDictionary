package com.artyomefimov.pocketdictionary.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.utils.view.sendResult

class ConfirmDeletionDialog : DialogFragment() {
    companion object {
        const val ELEMENT = "element"

        @JvmStatic
        fun newInstance(element: String?): ConfirmDeletionDialog =
            ConfirmDeletionDialog().apply {
                arguments = Bundle().apply {
                    putString(ELEMENT, element)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val element = arguments?.getString(ELEMENT)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_deletion, null)

        val dialog = AlertDialog.Builder(activity)
            .setTitle(R.string.confirm_deletion_dialog_title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                sendResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ELEMENT, element)
                })
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                sendResult(Activity.RESULT_CANCELED, Intent())
            }
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }
}