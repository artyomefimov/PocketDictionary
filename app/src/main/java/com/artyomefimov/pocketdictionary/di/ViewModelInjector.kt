package com.artyomefimov.pocketdictionary.di

import com.artyomefimov.pocketdictionary.ui.TranslationViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjector {
    fun inject(translationViewModel: TranslationViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}