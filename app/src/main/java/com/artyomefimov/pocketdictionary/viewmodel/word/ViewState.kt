package com.artyomefimov.pocketdictionary.viewmodel.word

import com.artyomefimov.pocketdictionary.R

enum class ViewState(
    val menuIcon: Int,
    val isEnabled: Boolean
) {
    EditingState(R.drawable.ic_action_edit_done, true),
    StableState(R.drawable.ic_action_edit, false)
}