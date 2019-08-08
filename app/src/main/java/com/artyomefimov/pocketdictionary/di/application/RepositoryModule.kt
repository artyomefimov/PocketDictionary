package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLocalStorage(translateApi: TranslateApi): Repository {
        return Repository(translateApi)
    }
}