package com.artyomefimov.pocketdictionary.di

import com.artyomefimov.pocketdictionary.storage.LocalStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StorageModule {
    @Provides
    @Singleton
    internal fun provideLocalStorage(): LocalStorage {
        return LocalStorage()
    }
}