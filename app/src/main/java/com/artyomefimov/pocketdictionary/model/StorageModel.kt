package com.artyomefimov.pocketdictionary.model

import java.io.Serializable

data class DictionaryRecord(
    var originalWord: String = "",
    val translations: MutableList<String> = ArrayList(),
    var isNotApiRequestWasPerformed: Boolean = false
) : Serializable