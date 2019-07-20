package com.artyomefimov.pocketdictionary.view.wordlist

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.FragmentListWordsBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.view.MainActivity
import com.artyomefimov.pocketdictionary.view.word.WordFragment
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
    private lateinit var binding: FragmentListWordsBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this)[WordListViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_words, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view_word_list.layoutManager = LinearLayoutManager(this.activity)
        recycler_view_word_list.adapter = WordListAdapter(ArrayList(),
            onItemClickAction = { dictionaryRecord -> openWordFragmentFor(dictionaryRecord) })

        fab_new_word.setOnClickListener { openWordFragmentFor(DictionaryRecord()) }

        viewModel.loadDictionary(
            onSuccessfulLoading = { receivedDictionary ->
                (recycler_view_word_list.adapter as WordListAdapter)
                    .updateDictionary(receivedDictionary)
            },
            onFailure = { errorMessage ->
                Toast.makeText(
                    this@WordListFragment.activity,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            })
    }

    private fun openWordFragmentFor(dictionaryRecord: DictionaryRecord) {
        if (this.activity != null) {
            val mainActivity = this.activity as MainActivity
            mainActivity.replaceFragment(WordFragment.newInstance(dictionaryRecord))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                (recycler_view_word_list.adapter as WordListAdapter)
                    .updateDictionary(viewModel.dictionary)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val result = viewModel.findRecords(query)
                (recycler_view_word_list.adapter as WordListAdapter)
                    .updateDictionary(result)
                return true
            }

            override fun onQueryTextChange(changedText: String?): Boolean {
                return false
            }
        })
    }
}