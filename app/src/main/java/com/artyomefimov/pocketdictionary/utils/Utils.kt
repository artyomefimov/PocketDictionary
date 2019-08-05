package com.artyomefimov.pocketdictionary.utils

fun <T>getMutableListOf(immutableList: List<T>) =
    mutableListOf<T>().apply {
        immutableList.forEach { this.add(it) }
    }