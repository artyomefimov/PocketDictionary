package com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel

import android.view.View
import com.artyomefimov.pocketdictionary.R

enum class ViewState(
    val menuIcon: Int,
    val visibility: Int,
    val isEnabled: Boolean
) {
    EditingState(
        R.drawable.ic_action_finish_edit,
        View.VISIBLE,
        true
    ),
    StableState(
        R.drawable.ic_action_edit,
        View.GONE,
        false
    )
}