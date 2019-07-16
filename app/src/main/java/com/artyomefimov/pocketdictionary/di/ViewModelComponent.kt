package com.artyomefimov.pocketdictionary.di

import com.artyomefimov.pocketdictionary.viewmodel.WordListViewModel
import com.artyomefimov.pocketdictionary.viewmodel.WordViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class])
interface ViewModelComponent {
    fun inject(wordViewModel: WordViewModel)

    fun inject(wordListViewModel: WordListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent
        fun networkModule(networkModule: NetworkModule): Builder
        fun storageModule(storageModule: StorageModule): Builder
    }
}