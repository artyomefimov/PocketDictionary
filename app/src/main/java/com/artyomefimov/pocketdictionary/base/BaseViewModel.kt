//package com.artyomefimov.pocketdictionary.base
//
//import android.arch.lifecycle.ViewModel
//import com.artyomefimov.pocketdictionary.di.DaggerViewModelComponent
//import com.artyomefimov.pocketdictionary.di.NetworkModule
//import com.artyomefimov.pocketdictionary.di.StorageModule
//import com.artyomefimov.pocketdictionary.di.ViewModelComponent
//import com.artyomefimov.pocketdictionary.viewmodel.WordViewModel
//
//abstract class BaseViewModel: ViewModel(){
//    protected abstract var injector: ViewModelComponent
//
//    init {
//        inject()
//    }
//
//    /**
//     * Injects the required dependencies
//     */
//    private fun inject() {
//
//
//    }
//
////    abstract fun onOperationStart()
////    abstract fun onOperationEnd()
////    abstract fun onOperationSuccess(response: Response)
////    abstract fun onOperationUnsuccess(error: Throwable)
//}