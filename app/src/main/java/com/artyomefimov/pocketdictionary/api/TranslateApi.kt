package com.artyomefimov.pocketdictionary.api

import com.artyomefimov.pocketdictionary.model.Response
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("/get")
    fun getTranslation(
        @Query("q") originalWord: String,
        @Query("langpair") languagesPair: String
    ): Single<Response>
}