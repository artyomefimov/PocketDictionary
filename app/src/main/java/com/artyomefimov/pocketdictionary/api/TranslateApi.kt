package com.artyomefimov.pocketdictionary.api

import com.artyomefimov.pocketdictionary.model.Response
import com.artyomefimov.pocketdictionary.model.Translation
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("/get")
    fun getTranslation(
        @Query("q") originalWord: String,
        @Query("langpair") languagesPair: String
    ): Observable<Response>
}