package com.artyomefimov.pocketdictionary.utils.binding

import android.content.ContextWrapper
import android.support.v4.app.Fragment
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.view.MainActivity

fun View.getParentFragment(): Fragment? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is MainActivity) {
            return context.supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        context = context.baseContext
    }
    return null
}