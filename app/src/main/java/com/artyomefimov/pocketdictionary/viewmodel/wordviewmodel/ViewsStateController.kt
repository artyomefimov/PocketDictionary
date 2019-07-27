package com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel

import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord

class ViewsStateController(
    private val dictionaryRecord: DictionaryRecord,
    private var isEditing: Boolean = false
) {

    fun setViewsStateAccordingToMode(originalWordText: EditText, submitButton: Button) {
        if (dictionaryRecord.originalWord.isEmpty()) {
            originalWordText.isEnabled = true

            submitButton.visibility = View.VISIBLE
            submitButton.isEnabled = true

            isEditing = true
        } else {
            originalWordText.isEnabled = false

            submitButton.visibility = View.GONE

            isEditing = false
        }
    }

    fun changeStateOf(editItem: MenuItem, originalWordText: EditText, submitButton: Button) {
        isEditing = if (isEditing) {
            editItem.setIcon(R.drawable.ic_action_edit)
            originalWordText.isEnabled = false

            submitButton.visibility = View.GONE

            false
        } else {
            editItem.setIcon(R.drawable.ic_action_finish_edit)

            originalWordText.isEnabled = true

            submitButton.visibility = View.VISIBLE
            submitButton.isEnabled = true

            true
        }
    }
}