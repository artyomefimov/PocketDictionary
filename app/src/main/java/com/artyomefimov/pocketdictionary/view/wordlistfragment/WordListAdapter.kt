package com.artyomefimov.pocketdictionary.view.wordlistfragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordListAdapter(
    var dictionaryRecords: List<DictionaryRecord>,
    private val onItemClickAction: (dictionaryRecord: DictionaryRecord) -> Unit
) :
    RecyclerView.Adapter<WordListAdapter.WordListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_word, parent, false)
        return WordListViewHolder(itemView)
    }

    override fun getItemCount(): Int =
        dictionaryRecords.size

    override fun onBindViewHolder(holder: WordListViewHolder, position: Int) =
        holder.bind(dictionaryRecords[position], onItemClickAction)

    class WordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            dictionaryRecord: DictionaryRecord,
            onItemClickAction: (dictionaryRecord: DictionaryRecord) -> Unit
        ) {
            with(itemView) {
                original_word_list_item.text = dictionaryRecord.originalWord
                translations_list_item.text = dictionaryRecord.translations[0]

                setOnClickListener {
                    onItemClickAction(dictionaryRecord)
                }
            }
        }
    }
}