package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.PocketDictionaryApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, TranslateModule::class])
interface ApplicationComponent {
    fun inject(application: PocketDictionaryApplication)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        fun networkModule(networkModule: TranslateModule): Builder
        fun storageModule(repositoryModule: RepositoryModule): Builder
    }
}