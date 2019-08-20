package com.artyomefimov.pocketdictionary.utils.view

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.shortToast(messageResId: Int) =
    Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show()

fun Fragment.longToast(messageResId: Int) =
    Toast.makeText(activity, messageResId, Toast.LENGTH_LONG).show()

fun Fragment.snackbar(messageAndChangedWord: Pair<Int, String>, action: (String) -> Unit) =
    Snackbar
        .make(
            view!!,
            messageAndChangedWord.first,
            Snackbar.LENGTH_INDEFINITE
        )
        .setAction(android.R.string.yes) {
            action(messageAndChangedWord.second)
        }
        .show()