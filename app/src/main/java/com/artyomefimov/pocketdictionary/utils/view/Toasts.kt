package com.artyomefimov.pocketdictionary.utils.view

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.shortToast(messageResId: Int) =
    Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show()

fun Fragment.longToast(messageResId: Int) =
    Toast.makeText(activity, messageResId, Toast.LENGTH_LONG).show()