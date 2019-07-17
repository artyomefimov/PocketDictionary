package com.artyomefimov.pocketdictionary.view.wordlistfragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.viewmodel.WordListViewModel
import kotlinx.android.synthetic.main.fragment_list_words.*

class WordListFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = WordListFragment().apply {
            arguments = Bundle.EMPTY
        }
    }

    private lateinit var viewModel: WordListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list_words, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this)[WordListViewModel::class.java]

        recycler_view_word_list.layoutManager = LinearLayoutManager(this.activity)
        recycler_view_word_list.adapter = WordListAdapter(ArrayList(),
            onItemClickAction = { dictionaryRecord ->
                Toast.makeText( // todo call word fragment
                    this@WordListFragment.activity,
                    "Clicked ${dictionaryRecord.originalWord}",
                    Toast.LENGTH_SHORT
                ).show()
            })

        fab_new_word.setOnClickListener {

        }

        viewModel.loadDictionary(
            onSuccessfulLoading = { receivedDictionary ->
                (recycler_view_word_list.adapter as WordListAdapter).apply {
                    dictionaryRecords = receivedDictionary
                    notifyDataSetChanged()
                }
            },
            onFailure = { errorMessage ->
                Toast.makeText(
                    this@WordListFragment.activity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            })
    }
}