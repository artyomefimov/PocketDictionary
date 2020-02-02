package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {
    @Provides
    fun provideLocalStorage(): LocalStorage =
        LocalStorage()

    @Provides
    fun provideRepository(translateApi: TranslateApi, localStorage: LocalStorage): Repository {
        return Repository(translateApi, localStorage)
    }
}