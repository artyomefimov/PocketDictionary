package com.artyomefimov.pocketdictionary.base

import android.arch.lifecycle.ViewModel
import com.artyomefimov.pocketdictionary.di.DaggerViewModelInjector
import com.artyomefimov.pocketdictionary.di.NetworkModule
import com.artyomefimov.pocketdictionary.di.StorageModule
import com.artyomefimov.pocketdictionary.di.ViewModelInjector
import com.artyomefimov.pocketdictionary.viewmodel.WordFragmentViewModel

abstract class BaseViewModel: ViewModel(){
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .storageModule(StorageModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is WordFragmentViewModel -> injector.inject(this)
        }
    }

//    abstract fun onOperationStart()
//    abstract fun onOperationEnd()
//    abstract fun onOperationSuccess(response: Response)
//    abstract fun onOperationUnsuccess(error: Throwable)
}