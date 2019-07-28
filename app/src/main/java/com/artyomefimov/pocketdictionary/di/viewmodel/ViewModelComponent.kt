package com.artyomefimov.pocketdictionary.di.viewmodel

import com.artyomefimov.pocketdictionary.viewmodel.wordviewmodel.WordViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {
    fun inject(wordViewModel: WordViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent
        fun networkModule(networkModule: NetworkModule): Builder
    }
}