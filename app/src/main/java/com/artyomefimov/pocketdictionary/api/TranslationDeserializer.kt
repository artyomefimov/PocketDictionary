package com.artyomefimov.pocketdictionary.api

import com.artyomefimov.pocketdictionary.model.Response
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class TranslationDeserializer: JsonDeserializer<Response> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Response =
        Gson().fromJson(json, Response::class.java)
}