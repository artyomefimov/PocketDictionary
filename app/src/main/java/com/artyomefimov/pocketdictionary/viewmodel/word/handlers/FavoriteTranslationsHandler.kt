package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

fun updateFavoriteTranslation(
    favoriteTranslations: MutableList<String>,
    translation: String?
): List<String> {
    translation?.let {
        if (favoriteTranslations.contains(it))
            favoriteTranslations.remove(it)
        else
            favoriteTranslations.add(it)
        return favoriteTranslations
    } ?: return favoriteTranslations
}