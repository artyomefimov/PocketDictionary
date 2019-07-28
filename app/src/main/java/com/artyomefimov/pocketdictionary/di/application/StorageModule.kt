package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.storage.LocalStorage
import dagger.Module
import dagger.Provides

@Module
object StorageModule {
    @Provides
    @ApplicationScope
    fun provideLocalStorage(): LocalStorage {
        return LocalStorage()
    }
}