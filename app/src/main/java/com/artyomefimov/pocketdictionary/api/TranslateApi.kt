package com.artyomefimov.pocketdictionary.api

import com.artyomefimov.pocketdictionary.model.Response
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Requests translation for specified word from https://api.mymemory.translated.net
 * @param [originalWord] a word for which translation will be requested
 * @param [languagesPair] a pair of a language of an original word and a target language of translation
 */
interface TranslateApi {
    @GET("/get")
    fun getTranslation(
        @Query("q") originalWord: String,
        @Query("langpair") languagesPair: String
    ): Single<Response>
}