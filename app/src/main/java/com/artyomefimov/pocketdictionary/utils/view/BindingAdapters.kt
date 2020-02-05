package com.artyomefimov.pocketdictionary.utils.view

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
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
            val navHostFragment: NavHostFragment =
                context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            return navHostFragment.childFragmentManager.fragments[0]
        }
        context = context.baseContext
    }
    return null
}