package com.artyomefimov.pocketdictionary.view.wordlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordListAdapter(
    var dictionary: List<DictionaryRecord>,
    private val onItemClickAction: (dictionaryRecord: DictionaryRecord) -> Unit
) :
    RecyclerView.Adapter<WordListAdapter.WordListViewHolder>() {

    fun updateDictionary(dictionaryRecords: List<DictionaryRecord>) {
        this.dictionary = dictionaryRecords
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_word, parent, false)
        return WordListViewHolder(itemView)
    }

    override fun getItemCount(): Int =
        dictionary.size

    override fun onBindViewHolder(holder: WordListViewHolder, position: Int) =
        holder.bind(dictionary[position], onItemClickAction)

    class WordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            dictionaryRecord: DictionaryRecord,
            onItemClickAction: (dictionaryRecord: DictionaryRecord) -> Unit
        ) {
            with(itemView) {
                original_word_list_item.text = dictionaryRecord.originalWord
                translations_list_item.text = dictionaryRecord.translations.getOrNull(0) // todo rethink logic

                setOnClickListener {
                    onItemClickAction(dictionaryRecord)
                }
            }
        }
    }
}