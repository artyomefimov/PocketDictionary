package com.artyomefimov.pocketdictionary.view.word

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_REQUEST_CODE
import com.artyomefimov.pocketdictionary.EDIT_TRANSLATION_DIALOG_TAG
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.FragmentWordBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.utils.LanguagePairs
import com.artyomefimov.pocketdictionary.view.word.EditTranslationDialog.Companion.POSITION
import com.artyomefimov.pocketdictionary.view.word.EditTranslationDialog.Companion.TRANSLATION
import com.artyomefimov.pocketdictionary.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_word.*

class WordFragment : Fragment() { // todo implement properly
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dictionaryRecord = arguments?.getSerializable(DICTIONARY_RECORD) as DictionaryRecord
        viewModel = ViewModelProviders.of(this, WordViewModel.Factory(dictionaryRecord))[WordViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        original_word_text.isEnabled = false // todo change when in edit mode

        recycler_view_translations.layoutManager = LinearLayoutManager(this.activity)
        recycler_view_translations.adapter = TranslationsAdapter(ArrayList(),
            onItemClicked = { translation, position ->
                showEditTranslationDialog(translation, position)
            })

        fab_add_translation.setOnClickListener {
            viewModel.addEmptyTranslation()
        }

        viewModel.originalWordLiveData.observe(this, Observer { originalWord ->
            original_word_text.setText(originalWord ?: "")
        })

        viewModel.translationsLiveData.observe(this, Observer { translations ->
            (recycler_view_translations.adapter as TranslationsAdapter)
                .updateTranslations(translations ?: listOf())
        })

        viewModel.errorMessage.observe(this, Observer { messageId ->
            Toast.makeText( // todo propose to add translations manually
                this.activity,
                messageId!!,
                Toast.LENGTH_SHORT
            )
                .show()
        })

        viewModel.loadOriginalWordTranslation(LanguagePairs.EnglishRussian)

        // todo add logic for closing fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                EDIT_TRANSLATION_DIALOG_REQUEST_CODE ->
                    viewModel.changeTranslation(
                        data?.getStringExtra(TRANSLATION),
                        data?.getIntExtra(POSITION, -1)
                    )
            }
        }
    }
}