package com.artyomefimov.pocketdictionary.utils.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import android.content.ContextWrapper
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import android.view.View
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.view.MainActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val currentFragment: Fragment? = view.getParentFragment()
    if (currentFragment != null && visibility != null) {
        visibility.observe(currentFragment, Observer { value -> view.visibility = value ?: View.GONE })
    }
}

private fun View.getParentFragment(): Fragment? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is MainActivity) {
            return context.supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        context = context.baseContext
    }
    return null
}