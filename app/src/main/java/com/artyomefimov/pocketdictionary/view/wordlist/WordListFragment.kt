package com.artyomefimov.pocketdictionary.view.wordlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val result = viewModel.findRecords(query)
                Log.d("WordListFragment", "received records: $result")
                // todo fix search view layout(show on full action bar)
                // todo hide search view if searching was cancelled
                // todo think how to show search results
                return true
            }

            override fun onQueryTextChange(changedText: String?): Boolean {
                return false
            }
        })
    }
}