package com.artyomefimov.pocketdictionary.utils

private val LATIN_INPUT_REGEX = "^[A-Za-z]+[A-Za-z\\s]*".toRegex()
private val CYRILLIC_INPUT_REGEX = "^[А-яа-яЁё]+[А-яа-яЁё\\s]*".toRegex()

fun isLatinInputCorrect(input: String): Boolean = input.matches(LATIN_INPUT_REGEX)
fun isLatinInputIncorrect(input: String): Boolean = !isLatinInputCorrect(input)

fun isCyrillicInputCorrect(input: String): Boolean = input.matches(CYRILLIC_INPUT_REGEX)