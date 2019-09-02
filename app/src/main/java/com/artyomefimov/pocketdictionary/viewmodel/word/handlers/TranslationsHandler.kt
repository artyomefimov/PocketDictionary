package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import android.util.Log
import com.artyomefimov.pocketdictionary.NEW_TRANSLATION_POSITION
import com.artyomefimov.pocketdictionary.utils.getMutableListOf
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.exceptions.DuplicateTranslationException

/**
 * Handles actions that are performed on translations (deletion, adding, changing)
 */
class TranslationsHandler {
    private companion object {
        const val TAG = "TranslationsHandler"
    }

    fun deleteTranslation(
        translation: String,
        translationsLiveDataValue: List<String>
    ): MutableList<String> {
        val mutableTranslations = getMutableListOf(translationsLiveDataValue)
        mutableTranslations.remove(translation)
        return mutableTranslations
    }

    @Throws(DuplicateTranslationException::class)
    fun handleNewTranslationOnPosition(
        changedTranslation: String?,
        position: Int?,
        translationsLiveDataValue: List<String>
    ): MutableList<String> {
        val mutableTranslations = getMutableListOf(translationsLiveDataValue)

        return if (NEW_TRANSLATION_POSITION == position)
            addTranslation(changedTranslation!!, mutableTranslations)
        else
            changeTranslation(changedTranslation, position, mutableTranslations)
    }

    @Throws(DuplicateTranslationException::class)
    fun addTranslation(
        translation: String,
        mutableTranslations: MutableList<String>
    ): MutableList<String> {
        if (isNewTranslationNotDuplicate(translation, mutableTranslations))
            mutableTranslations.add(translation)
        else
            throw DuplicateTranslationException()
        return mutableTranslations
    }

    private fun isNewTranslationNotDuplicate(translation: String, mutableTranslations: MutableList<String>) =
        !mutableTranslations.contains(translation)

    private fun changeTranslation(
        changedTranslation: String?,
        position: Int?,
        mutableTranslations: MutableList<String>
    ): MutableList<String> {
        if (isReceivedDataValid(changedTranslation, position)) {
            mutableTranslations[position!!] = changedTranslation!!
        } else {
            Log.d(TAG, "Invalid translation $changedTranslation and position $position")
        }
        return mutableTranslations
    }

    private fun isReceivedDataValid(changedTranslation: String?, position: Int?) =
        changedTranslation != null && position != null && position != -1
}