package com.artyomefimov.pocketdictionary.view.word

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.artyomefimov.pocketdictionary.*
import com.artyomefimov.pocketdictionary.databinding.FragmentWordBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.services.StorageUpdateService
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.utils.view.shortToast
import com.artyomefimov.pocketdictionary.utils.view.showDialog
import com.artyomefimov.pocketdictionary.utils.view.snackbar
import com.artyomefimov.pocketdictionary.view.adapters.TranslationsAdapter
import com.artyomefimov.pocketdictionary.view.dialogs.ConfirmDeletionDialog
import com.artyomefimov.pocketdictionary.view.dialogs.ConfirmDeletionDialog.Companion.ELEMENT
import com.artyomefimov.pocketdictionary.view.dialogs.EditTranslationDialog
import com.artyomefimov.pocketdictionary.view.dialogs.EditTranslationDialog.Companion.POSITION
import com.artyomefimov.pocketdictionary.view.dialogs.EditTranslationDialog.Companion.TRANSLATION
import com.artyomefimov.pocketdictionary.viewmodel.word.WordViewModel
import com.artyomefimov.pocketdictionary.viewmodel.word.handlers.ViewState
import kotlinx.android.synthetic.main.fragment_word.*

class WordFragment : Fragment() {
    companion object {
        private const val DICTIONARY_RECORD = "dictionaryRecord"

        @JvmStatic
        fun newInstance(dictionaryRecord: DictionaryRecord?) =
            WordFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DICTIONARY_RECORD, dictionaryRecord)
                }
            }
    }

    private lateinit var binding: FragmentWordBindingImpl
    private lateinit var viewModel: WordViewModel
    private lateinit var initialViewState: ViewState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewStateFromBundle = savedInstanceState?.getSerializable(VIEW_STATE)
        initialViewState = if (viewStateFromBundle != null)
            viewStateFromBundle as ViewState
        else ViewState.STABLE_STATE

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = initViewModel(DICTIONARY_RECORD)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.setInitialViewState(initialViewState)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_word_fragment, menu)

        val editItem = menu.findItem(R.id.action_edit)
        viewModel.getInitialViewState().apply {
            initialViewState = this
            applyNewStateFor(this, editItem, original_word_text)
        }

        editItem?.setOnMenuItemClickListener {
            viewModel.getNewState(original_word_text.text.toString()).apply {
                initialViewState = this
                applyNewStateFor(this, editItem, original_word_text)
            }
            return@setOnMenuItemClickListener true
        }

        val undoItem = menu.findItem(R.id.action_undo)
        undoItem?.setOnMenuItemClickListener {
            viewModel.undoChanges().apply {
                applyNewStateFor(this, editItem, original_word_text)
            }

            return@setOnMenuItemClickListener true
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view_translations.layoutManager =
            LinearLayoutManager(this.activity)
        recycler_view_translations.adapter =
            TranslationsAdapter<String>(ArrayList(), viewModel.currentFavoriteTranslations,
                onClickAction = { translation, position ->
                    showDialog<EditTranslationDialog>(translation, position)
                },
                onLongClickAction = { translation ->
                    showDialog<ConfirmDeletionDialog>(translation, -1)
                },
                onTranslationChecked = { translation ->
                    viewModel.updateFavoriteTranslations(translation)
                })

        fab_add_translation.setOnClickListener {
            showDialog<EditTranslationDialog>("", NEW_TRANSLATION_POSITION)
        }

        viewModel.originalWordLiveData.observe(viewLifecycleOwner, Observer { originalWord ->
            original_word_text.setText(originalWord ?: "")
        })

        viewModel.translationsLiveData.observe(viewLifecycleOwner, Observer { translations ->
            (recycler_view_translations.adapter as TranslationsAdapter<String>)
                .updateData(translations ?: listOf())
        })

        viewModel.toastMessageLiveData.observe(viewLifecycleOwner, Observer { messageResId ->
            shortToast(messageResId)
        })

        viewModel.snackbarMessageLiveData.observe(
            viewLifecycleOwner,
            Observer { messageAndChangedWord ->
                snackbar(messageAndChangedWord) { changedWord ->
                    viewModel.loadOriginalWordTranslation(
                        changedWord,
                        LanguagePairs.FromEnglishToRussian
                    )
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                EDIT_TRANSLATION_DIALOG_REQUEST_CODE ->
                    viewModel.handleNewTranslationOnPosition(
                        data?.getStringExtra(TRANSLATION),
                        data?.getIntExtra(POSITION, -1)
                    )
                CONFIRM_DELETION_DIALOG_REQUEST_CODE ->
                    viewModel.deleteTranslation(
                        data?.getStringExtra(ELEMENT)
                    )
            }
        }
    }

    override fun onDestroy() {
        activity?.startService(
            Intent(activity, StorageUpdateService::class.java)
        )
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(VIEW_STATE, initialViewState)
    }
}