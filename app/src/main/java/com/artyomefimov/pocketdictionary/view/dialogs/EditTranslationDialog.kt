package com.artyomefimov.pocketdictionary.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import com.artyomefimov.pocketdictionary.utils.isCyrillicInputCorrect
import com.artyomefimov.pocketdictionary.utils.view.sendResult
import com.artyomefimov.pocketdictionary.utils.view.shortToast
import com.artyomefimov.pocketdictionary.R as pdR

class EditTranslationDialog : DialogFragment() {
    companion object {
        const val TRANSLATION = "translation"
        const val POSITION = "position"

        @JvmStatic
        fun newInstance(translation: String?, position: Int): EditTranslationDialog =
            EditTranslationDialog().apply {
                arguments = Bundle().apply {
                    putString(TRANSLATION, translation)
                    putInt(POSITION, position)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val position = arguments?.getInt(POSITION)
        val translation = arguments?.getString(TRANSLATION)

        val view = LayoutInflater.from(activity).inflate(pdR.layout.dialog_edit_translation, null)

        val editText = view.findViewById<EditText>(pdR.id.translation_edit)
        editText.setText(translation)
        editText.setSelection(editText.text.length)

        val dialog = AlertDialog.Builder(activity)
            .setTitle(pdR.string.edit_translation_dialog_title)
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(android.R.string.cancel, null)
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val changedTranslation = editText.text.toString().trim()

//                if (isCyrillicInputCorrect(changedTranslation)) {
//
//                } else
//                    showErrorMessage()

                sendResult(Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(TRANSLATION, changedTranslation)
                        putExtra(POSITION, position)
                    })
                dialog.dismiss()
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                sendResult(Activity.RESULT_CANCELED, Intent())
                dialog.dismiss()
            }
        }
        return dialog
    }

    private fun showErrorMessage() =
        shortToast(pdR.string.incorrect_translation)
}