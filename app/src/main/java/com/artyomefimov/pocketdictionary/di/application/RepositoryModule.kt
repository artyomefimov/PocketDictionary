package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.repository.Repository
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {
    @Provides
    fun provideLocalStorage(): LocalStorage =
        LocalStorage()

    @Provides
    fun provideGson(): Gson =
        Gson()

    @Provides
    fun provideRepository(translateApi: TranslateApi, localStorage: LocalStorage, gson: Gson): Repository {
        return Repository(translateApi, localStorage, gson)
    }
}