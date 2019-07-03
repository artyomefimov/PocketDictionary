package com.artyomefimov.pocketdictionary.di

import com.artyomefimov.pocketdictionary.BASE_URL
import com.artyomefimov.pocketdictionary.api.TranslateApi
import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.api.TranslationDeserializer
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideTranslateApi(retrofit: Retrofit): TranslateApi =
        retrofit.create(TranslateApi::class.java)

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(
                Response::class.java,
                TranslationDeserializer()
            )
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}