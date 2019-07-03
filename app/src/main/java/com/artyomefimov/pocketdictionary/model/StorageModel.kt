package com.artyomefimov.pocketdictionary.model

import java.io.Serializable

class Translation(
    val originalWord: String,
    var translations: MutableList<String>? = ArrayList()
) : Serializable {

    fun addTranslation(translation: String) {
        translations?.add(translation)
    }

    fun removeTranslation(translation: String) {
        translations?.remove(translation)
    }
}