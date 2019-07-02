package com.artyomefimov.pocketdictionary.model

import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName(value = "translatedText")
    val translatedText: String
)

data class Response(
    @SerializedName("responseData")
    val responseData: Translation
)