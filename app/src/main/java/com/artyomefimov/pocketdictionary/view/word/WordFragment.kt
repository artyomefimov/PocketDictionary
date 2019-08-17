package com.artyomefimov.pocketdictionary.view.word

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.artyomefimov.pocketdictionary.CONFIRM_DELETION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.NEW_TRANSLATION_POSITION
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.FragmentWordBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.services.StorageUpdateService
import com.artyomefimov.pocketdictionary.utils.showConfirmDeletionDialog
import com.artyomefimov.pocketdictionary.utils.showEditTranslationDialog
import com.artyomefimov.pocketdictionary.view.ConfirmDeletionDialog.Companion.ELEMENT
import com.artyomefimov.pocketdictionary.view.word.EditTranslationDialog.Companion.POSITION
import com.artyomefimov.pocketdictionary.view.word.EditTranslationDialog.Companion.TRANSLATION
import com.artyomefimov.pocketdictionary.viewmodel.word.WordViewModel
import kotlinx.android.synthetic.main.fragment_word.*

class WordFragment : Fragment() {
    companion object {
        private const val DICTIONARY_RECORD = "dictionaryRecord"

        @JvmStatic
        fun newInstance(dictionaryRecord: DictionaryRecord) =
            WordFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DICTIONARY_RECORD, dictionaryRecord)
                }
            }
    }

    private lateinit var binding: FragmentWordBindingImpl
    private lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_word_fragment, menu)
        val editItem = menu?.findItem(R.id.action_edit)!!
        viewModel.getInitialViewState().apply {
            applyNewStateFor(this, editItem, original_word_text)
        }

        editItem.setOnMenuItemClickListener {
            viewModel.getNewState(original_word_text.text.toString()).apply {
                applyNewStateFor(this, editItem, original_word_text)
            }
            return@setOnMenuItemClickListener true
        }

        val undoItem = menu.findItem(R.id.action_undo)!!
        undoItem.setOnMenuItemClickListener {
            viewModel.undoChanges().apply {
                applyNewStateFor(this, editItem, original_word_text)
            }

            return@setOnMenuItemClickListener true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = initViewModel(DICTIONARY_RECORD)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view_translations.layoutManager = LinearLayoutManager(this.activity)
        recycler_view_translations.adapter = TranslationsAdapter(ArrayList(),
            onClickAction = { translation, position -> showEditTranslationDialog(translation, position) },
            onLongClickAction = { translation -> showConfirmDeletionDialog(translation) })

        fab_add_translation.setOnClickListener {
            showEditTranslationDialog("", NEW_TRANSLATION_POSITION)
        }

        viewModel.originalWordLiveData.observe(this, Observer { originalWord ->
            original_word_text.setText(originalWord ?: "")
        })

        viewModel.translationsLiveData.observe(this, Observer { translations ->
            (recycler_view_translations.adapter as TranslationsAdapter)
                .updateTranslations(translations ?: listOf())
        })

        viewModel.messageLiveData.observe(this, Observer { messageId ->
            Toast.makeText(
                this.activity,
                messageId!!,
                Toast.LENGTH_SHORT
            )
                .show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                EDIT_TRANSLATION_DIALOG_REQUEST_CODE -> viewModel.handleNewTranslationOnPosition(
                    data?.getStringExtra(TRANSLATION),
                    data?.getIntExtra(POSITION, -1)
                )
                CONFIRM_DELETION_DIALOG_REQUEST_CODE -> viewModel.deleteTranslation(
                    data?.getStringExtra(ELEMENT)!!
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateDictionary {
            activity?.startService(
                Intent(activity, StorageUpdateService::class.java)
            )
        }
    }
}