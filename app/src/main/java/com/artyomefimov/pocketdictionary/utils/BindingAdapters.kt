package com.artyomefimov.pocketdictionary.utils

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val currentFragment: Fragment? = view.getParentFragment()
    if (currentFragment != null && visibility != null) {
        visibility.observe(currentFragment, Observer { value -> view.visibility = value ?: View.GONE })
    }
}

@BindingAdapter("originalWord")
fun setOriginalWord(view: EditText, originalWord: MutableLiveData<String>?) {
    val currentFragment: Fragment? = view.getParentFragment()
    if (currentFragment != null && originalWord != null) {
        originalWord.observe(currentFragment, Observer { value -> view.setText(value ?: "") })
    }
}