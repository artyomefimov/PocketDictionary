package com.artyomefimov.pocketdictionary.utils.view

import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.widget.Toast

fun Fragment.shortToast(messageResId: Int?) {
    Toast.makeText(activity, messageResId ?: return, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(messageResId: Int?) {
    Toast.makeText(activity, messageResId ?: return, Toast.LENGTH_LONG).show()
}

fun Fragment.snackbar(messageAndChangedWord: Pair<Int, String>?, action: (String) -> Unit) {
    Snackbar
        .make(
            view ?: return,
            messageAndChangedWord?.first ?: return,
            Snackbar.LENGTH_INDEFINITE
        )
        .setAction(android.R.string.yes) {
            action(messageAndChangedWord.second)
        }
        .show()
}