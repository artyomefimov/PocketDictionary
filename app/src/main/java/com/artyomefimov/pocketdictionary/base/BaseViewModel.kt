package com.artyomefimov.pocketdictionary.base

import android.arch.lifecycle.ViewModel
import com.artyomefimov.pocketdictionary.di.DaggerViewModelInjector
import com.artyomefimov.pocketdictionary.di.NetworkModule
import com.artyomefimov.pocketdictionary.di.ViewModelInjector
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.model.Translation
import com.artyomefimov.pocketdictionary.ui.TranslationViewModel
import java.lang.Exception

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is TranslationViewModel -> injector.inject(this)
        }
    }

//    abstract fun onOperationStart()
//    abstract fun onOperationEnd()
//    abstract fun onOperationSuccess(response: Response)
//    abstract fun onOperationUnsuccess(error: Throwable)
}