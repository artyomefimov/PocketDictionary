package com.artyomefimov.pocketdictionary.view.wordlist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.artyomefimov.pocketdictionary.CONFIRM_DELETION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.PERMISSIONS_REQUEST_CODE
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.FragmentListWordsBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.services.StorageUpdateService
import com.artyomefimov.pocketdictionary.utils.view.shortToast
import com.artyomefimov.pocketdictionary.utils.view.showDialog
import com.artyomefimov.pocketdictionary.view.adapters.WordListAdapter
import com.artyomefimov.pocketdictionary.view.dialogs.ConfirmDeletionDialog
import com.artyomefimov.pocketdictionary.view.dialogs.ConfirmDeletionDialog.Companion.ELEMENT
import com.artyomefimov.pocketdictionary.view.isPermissionsGranted
import com.artyomefimov.pocketdictionary.view.needed_permissions
import com.artyomefimov.pocketdictionary.viewmodel.wordlist.WordListViewModel
import kotlinx.android.synthetic.main.fragment_list_words.*

class WordListFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = WordListFragment().apply {
            arguments = Bundle.EMPTY
        }
    }

    internal lateinit var viewModel: WordListViewModel
    private lateinit var binding: FragmentListWordsBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = initViewModel()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_words, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view_word_list.layoutManager = LinearLayoutManager(this.activity)
        recycler_view_word_list.adapter =
            WordListAdapter<DictionaryRecord>(ArrayList(),
                onClickAction = { dictionaryRecord ->
                    openWordFragmentFor(dictionaryRecord)
                },
                onLongClickAction = { originalWord ->
                    showDialog<ConfirmDeletionDialog>(originalWord, -1)
                })

        fab_new_word.setOnClickListener {
            openWordFragmentFor(DictionaryRecord())
        }

        viewModel.dictionaryLiveData.observe(this, Observer { dictionary ->
            showDictionary(dictionary!!)
        })

        viewModel.messageLiveData.observe(this, Observer { messageResId ->
            shortToast(messageResId!!)
        })

        if (isPermissionsGranted(activity as Activity, needed_permissions)) {
            loadDictionary()
        } else {
            requestPermissions()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_list_fragment, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                showDictionary(viewModel.dictionary)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.findRecords(query) {
                    showSearchResults(it)
                }
                return true
            }

            override fun onQueryTextChange(changedText: String?): Boolean = false
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (isRequestCodeCorrectAndPermissionsGranted(requestCode, grantResults))
            loadDictionary()
        else
            showPermissionsMessage()
    }

    private fun isRequestCodeCorrectAndPermissionsGranted(requestCode: Int, grantResults: IntArray) =
        requestCode == PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CONFIRM_DELETION_DIALOG_REQUEST_CODE ->
                    viewModel.deleteDictionaryRecord(data?.getStringExtra(ELEMENT)!!) {
                        activity?.startService(
                            Intent(activity, StorageUpdateService::class.java)
                        )
                    }
            }
        }
    }
}