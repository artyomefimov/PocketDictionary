package com.artyomefimov.pocketdictionary.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import kotlinx.android.synthetic.main.list_item_translation.view.*

class TranslationsAdapter<T>(
    translations: List<T>,
    private val onClickAction: (String, Int) -> Unit,
    private val onLongClickAction: (String) -> Unit
) :
    AbstractAdapter<T>(translations) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationsViewHolder<T> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_translation, parent, false)
        return TranslationsViewHolder(itemView, onClickAction)
    }

    override fun getItemCount(): Int =
        data.size

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) =
        holder.bind(data[position], position, onLongClickAction)
}

class TranslationsViewHolder<T>(
    itemView: View,
    private val onClickAction: (translation: String, position: Int) -> Unit
) : AbstractViewHolder<T>(itemView) {

    override fun bind(item: T, position: Int, onLongClickAction: (String) -> Unit) {
        with(itemView) {
            val translation = item as String

            translation_text.text = translation
            setOnClickListener {
                onClickAction(translation, position)
            }
            setOnLongClickListener {
                onLongClickAction(translation)
                true
            }
        }
    }
}