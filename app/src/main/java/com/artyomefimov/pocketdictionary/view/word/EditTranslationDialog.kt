package com.artyomefimov.pocketdictionary.view.word

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.utils.isCyrillicInputCorrect

class EditTranslationDialog : DialogFragment() {
    companion object {
        const val TRANSLATION = "translation"
        const val POSITION = "position"

        fun newInstance(translation: String, position: Int): EditTranslationDialog =
            EditTranslationDialog().apply {
                arguments = Bundle().apply {
                    putString(TRANSLATION, translation)
                    putInt(POSITION, position)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val position = arguments?.getInt(POSITION)!!
        val translation = arguments?.getString(TRANSLATION)!!

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_edit_translation, null)

        val editText = view.findViewById<EditText>(R.id.translation_edit)
        editText.setText(translation)

        val dialog = AlertDialog.Builder(activity)
            .setTitle(R.string.dialog_title)
            .setPositiveButton(R.string.dialog_ok) { _, _ ->
                val changedTranslation = editText.text.toString().trim()

                if (isCyrillicInputCorrect(changedTranslation))
                    sendOkResult(position, changedTranslation)
                else
                    showErrorMessage()

            }
            .setNegativeButton(R.string.dialog_cancel) { _, _ -> sendCancelledResult()}
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    private fun sendOkResult(position: Int, translation: String) =
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(POSITION, position)
                putExtra(TRANSLATION, translation)
            })

    private fun showErrorMessage() =
        Toast.makeText(activity, R.string.incorrect_translation, Toast.LENGTH_SHORT).show()

    private fun sendCancelledResult() =
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_CANCELED,
            Intent()
        )
}