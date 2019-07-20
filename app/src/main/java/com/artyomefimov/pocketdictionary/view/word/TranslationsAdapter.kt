package com.artyomefimov.pocketdictionary.view.word

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import kotlinx.android.synthetic.main.list_item_translation.view.*

class TranslationsAdapter(
    private var translations: List<String>,
    private val onTranslationChanged: (s: String, position: Int) -> Unit
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
        holder.bind(translations[position], position, onTranslationChanged)

    class TranslationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            translation: String,
            position: Int,
            onTranslationChanged: (translation: String, position: Int) -> Unit
        ) {
            with(itemView) {
                translation_text.setText(translation)
                translation_text.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        onTranslationChanged(s.toString(), position)
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }
}