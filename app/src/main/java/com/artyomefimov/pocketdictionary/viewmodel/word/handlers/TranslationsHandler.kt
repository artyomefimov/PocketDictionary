package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

import android.util.Log
import com.artyomefimov.pocketdictionary.NEW_TRANSLATION_POSITION
import com.artyomefimov.pocketdictionary.utils.getMutableListOf

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

    fun addTranslation(
        translation: String,
        mutableTranslations: MutableList<String>
    ): MutableList<String> {
        mutableTranslations.add(translation)
        return mutableTranslations
    }

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