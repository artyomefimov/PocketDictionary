package com.artyomefimov.pocketdictionary

import android.app.Application
import com.artyomefimov.pocketdictionary.di.application.ApplicationComponent
import com.artyomefimov.pocketdictionary.di.application.DaggerApplicationComponent
import com.artyomefimov.pocketdictionary.di.application.StorageModule
import com.artyomefimov.pocketdictionary.storage.LocalStorage
import javax.inject.Inject

class BaseApp : Application() {
    @Inject
    lateinit var localStorage: LocalStorage
    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        setup()
    }

    private fun setup() {
        component = DaggerApplicationComponent.builder()
            .storageModule(StorageModule)
            .build()
        component.inject(this)
    }
}