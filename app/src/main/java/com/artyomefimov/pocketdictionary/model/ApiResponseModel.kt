package com.artyomefimov.pocketdictionary.model

import com.google.gson.annotations.SerializedName

/**
 * Classes that help to parse json response from translation API
 */
data class TranslatedText(
    @SerializedName(value = "translatedText")
    val translatedText: String
)

data class Response(
    @SerializedName("responseData")
    val responseData: TranslatedText
)