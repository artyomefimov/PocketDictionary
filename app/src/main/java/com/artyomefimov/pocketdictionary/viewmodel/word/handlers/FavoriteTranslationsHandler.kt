package com.artyomefimov.pocketdictionary.viewmodel.word.handlers

fun updateFavoriteTranslation(
    favoriteTranslations: MutableList<String>,
    translation: String
): List<String> {
    if (favoriteTranslations.contains(translation))
        favoriteTranslations.remove(translation)
    else
        favoriteTranslations.add(translation)
    return favoriteTranslations
}