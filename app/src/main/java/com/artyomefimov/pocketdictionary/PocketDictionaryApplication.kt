package com.artyomefimov.pocketdictionary

import android.app.Application
import android.content.Context
import com.artyomefimov.pocketdictionary.di.application.ApplicationComponent
import com.artyomefimov.pocketdictionary.di.application.DaggerApplicationComponent
import com.artyomefimov.pocketdictionary.di.application.RepositoryModule
import com.artyomefimov.pocketdictionary.di.application.TranslateModule
import com.artyomefimov.pocketdictionary.repository.Repository
import javax.inject.Inject

class PocketDictionaryApplication : Application() {
    @Inject
    lateinit var repository: Repository
    private val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .networkModule(TranslateModule)
            .storageModule(RepositoryModule)
            .build()
    }

    companion object {
        @JvmStatic
        fun repository(context: Context) =
            (context.applicationContext as PocketDictionaryApplication).repository
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}