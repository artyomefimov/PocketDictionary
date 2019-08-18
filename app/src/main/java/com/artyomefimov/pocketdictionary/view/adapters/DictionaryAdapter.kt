package com.artyomefimov.pocketdictionary.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordListAdapter<T>(
    dictionary: List<T>,
    private val onClickAction: (DictionaryRecord) -> Unit,
    private val onLongClickAction: (String) -> Unit
) : AbstractAdapter<T>(dictionary) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<T> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_word, parent, false)
        return WordListViewHolder(itemView, onClickAction)
    }

    override fun getItemCount(): Int =
        data.size

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) =
        holder.bind(data[position], position, onLongClickAction)
}

private class WordListViewHolder<T>(
    itemView: View,
    private val onClickAction: (DictionaryRecord) -> Unit
) : AbstractViewHolder<T>(itemView) {

    override fun bind(item: T, position: Int, onLongClickAction: (String) -> Unit) {
        with(itemView) {
            val dictionaryRecord = item as DictionaryRecord
            original_word_list_item.text = dictionaryRecord.originalWord
            translations_list_item.text = dictionaryRecord.translations.getOrElse(0) { "" }

            setOnClickListener {
                onClickAction(dictionaryRecord)
            }
            setOnLongClickListener {
                onLongClickAction(dictionaryRecord.originalWord)
                true
            }
        }
    }
}