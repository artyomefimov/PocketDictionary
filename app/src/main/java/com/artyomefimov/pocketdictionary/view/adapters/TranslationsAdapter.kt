package com.artyomefimov.pocketdictionary.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import kotlinx.android.synthetic.main.list_item_translation.view.*

class TranslationsAdapter<T>(
    translations: List<T>,
    private val favoriteTranslations: List<String>,
    private val onClickAction: (String?, Int) -> Unit,
    private val onLongClickAction: (String?) -> Unit,
    private val onTranslationChecked: (String?) -> Unit
) :
    AbstractAdapter<T>(translations) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationsViewHolder<T> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_translation, parent, false)
        return TranslationsViewHolder(
            itemView,
            favoriteTranslations,
            onClickAction,
            onTranslationChecked
        )
    }

    override fun getItemCount(): Int {
        return if (data == null) {
            Log.d(
                TranslationsAdapter::class.java.simpleName,
                "Empty data collection!"
            )
            0
        } else {
            data!!.size
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) =
        holder.bind(data?.get(position), position, onLongClickAction)
}

class TranslationsViewHolder<T>(
    itemView: View,
    private val favoriteTranslations: List<String>,
    private val onClickAction: (translation: String?, position: Int) -> Unit,
    private val onTranslationChecked: (String?) -> Unit
) : AbstractViewHolder<T>(itemView) {

    override fun bind(item: T?, position: Int, onLongClickAction: (String?) -> Unit) {
        with(itemView) {
            val translation = item as? String
            translation_text.text = translation

            favorite_translation.isChecked = favoriteTranslations.contains(translation)

            translation_text.setOnClickListener {
                onClickAction(translation, position)
            }
            translation_text.setOnLongClickListener {
                onLongClickAction(translation)
                true
            }
            favorite_translation.setOnClickListener {
                onTranslationChecked(translation)
            }
        }
    }
}