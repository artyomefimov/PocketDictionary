package com.artyomefimov.pocketdictionary.di

import com.artyomefimov.pocketdictionary.viewmodel.TranslationViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface ViewModelInjector {
    fun inject(translationViewModel: TranslationViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun storageModule(storageModule: StorageModule): Builder
    }
}