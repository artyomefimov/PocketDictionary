package com.artyomefimov.pocketdictionary.di.application

import com.artyomefimov.pocketdictionary.BaseApp
import dagger.Component

@ApplicationScope
@Component(modules = [StorageModule::class])
interface ApplicationComponent {
    fun inject(application: BaseApp)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        fun storageModule(storageModule: StorageModule): Builder
    }
}