package com.artyomefimov.pocketdictionary.storage

import android.os.Environment
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject

class LocalStorage @Inject constructor() {

    companion object {
        private val STORAGE_PATH = "${Environment.getExternalStorageDirectory().absolutePath}/pocketdictionary.backup"
        private const val NO_SUCH_WORD = "No such word"
    }

    private var words: MutableMap<String, MutableList<String>> = HashMap()

    fun addTranslations(originalWord: String, translations: MutableList<String>) {
        words.remove(originalWord)
        words[originalWord] = translations
    }

    fun addTranslation(originalWord: String, translation: String) =
        words[originalWord]?.add(translation) ?: throw Exception(NO_SUCH_WORD)

    fun getTranslations(originalWord: String): MutableList<String> =
        words[originalWord] ?: mutableListOf()

    fun removeTranslations(originalWord: String) =
        words.remove(originalWord)

    fun removeTranslation(originalWord: String, translation: String) =
        words[originalWord]?.remove(translation)

    fun writeData() =
        ObjectOutputStream(FileOutputStream(STORAGE_PATH)).use {
            it.writeObject(words)
        }

    fun readData(): MutableMap<String, MutableList<String>> =
        ObjectInputStream(FileInputStream(STORAGE_PATH)).use {
            return it.readObject() as MutableMap<String, MutableList<String>>
        }
}