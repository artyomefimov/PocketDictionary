package com.artyomefimov.pocketdictionary.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.getTwoFavoriteTranslationsAsString
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordListAdapter<T>(
    dictionary: List<T>,
    private val onClickAction: (DictionaryRecord?) -> Unit,
    private val onLongClickAction: (String?) -> Unit
) : AbstractAdapter<T>(dictionary) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_word, parent, false)
        return WordListViewHolder(itemView, onClickAction)
    }

    override fun getItemCount(): Int {
        return if (data == null) {
            Log.d(
                WordListAdapter::class.java.simpleName,
                "Empty data collection!")
            0
        } else { data!!.size }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) =
        holder.bind(data?.get(position), position, onLongClickAction)
}

class WordListViewHolder<T>(
    itemView: View,
    private val onClickAction: (DictionaryRecord?) -> Unit
) : AbstractViewHolder<T>(itemView) {

    override fun bind(item: T?, position: Int, onLongClickAction: (String?) -> Unit) {
        with(itemView) {
            val dictionaryRecord = item as? DictionaryRecord
            original_word_list_item.text = dictionaryRecord?.originalWord ?: ""
            translations_list_item.text = getTwoFavoriteTranslationsAsString(dictionaryRecord)

            setOnClickListener {
                onClickAction(dictionaryRecord)
            }
            setOnLongClickListener {
                onLongClickAction(dictionaryRecord?.originalWord)
                true
            }
        }
    }
}