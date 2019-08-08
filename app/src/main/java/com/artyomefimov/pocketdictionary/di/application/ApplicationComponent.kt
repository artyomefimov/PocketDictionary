package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.PocketDictionaryApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun inject(application: PocketDictionaryApplication)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        fun networkModule(networkModule: NetworkModule): Builder
        fun storageModule(storageModule: RepositoryModule): Builder
    }
}