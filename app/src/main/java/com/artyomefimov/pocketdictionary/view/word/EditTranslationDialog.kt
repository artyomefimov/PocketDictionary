package com.artyomefimov.pocketdictionary.view.word

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.EditText
import com.artyomefimov.pocketdictionary.R

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
                targetFragment?.onActivityResult(
                    targetRequestCode,
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(POSITION, position)
                        putExtra(TRANSLATION, editText.text.toString())
                    })
            }
            .setNegativeButton(R.string.dialog_cancel) { _, _ ->
                targetFragment?.onActivityResult(
                    targetRequestCode,
                    Activity.RESULT_CANCELED,
                    Intent()
                )
            }
            .setView(view)
            .create()

        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }
}