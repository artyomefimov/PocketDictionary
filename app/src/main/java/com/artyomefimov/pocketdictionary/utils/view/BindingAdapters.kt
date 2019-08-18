package com.artyomefimov.pocketdictionary.utils.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v4.app.Fragment
import android.view.View

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val currentFragment: Fragment? = view.getParentFragment()
    if (currentFragment != null && visibility != null) {
        visibility.observe(currentFragment, Observer { value -> view.visibility = value ?: View.GONE })
    }
}