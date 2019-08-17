package com.artyomefimov.pocketdictionary.view.word

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import kotlinx.android.synthetic.main.list_item_translation.view.*

class TranslationsAdapter(
    private var translations: List<String>,
    private val onClickAction: (translation: String, position: Int) -> Unit,
    private val onLongClickAction: (translation: String) -> Unit
) :
    RecyclerView.Adapter<TranslationsAdapter.TranslationsViewHolder>() {

    fun updateTranslations(translations: List<String>) {
        this.translations = translations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_translation, parent, false)
        return TranslationsViewHolder(itemView)
    }

    override fun getItemCount(): Int =
        translations.size

    override fun onBindViewHolder(holder: TranslationsViewHolder, position: Int) =
        holder.bind(translations[position], position, onClickAction, onLongClickAction)

    class TranslationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            translation: String,
            position: Int,
            onTranslationChanged: (translation: String, position: Int) -> Unit,
            onLongClickAction: (translation: String) -> Unit
        ) {
            with(itemView) {
                translation_text.text = translation
                setOnClickListener {
                    onTranslationChanged(translation, position)
                }
                setOnLongClickListener {
                    onLongClickAction(translation)
                    true
                }
            }
        }
    }
}