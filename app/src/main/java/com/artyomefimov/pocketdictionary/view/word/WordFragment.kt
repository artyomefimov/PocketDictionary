package com.artyomefimov.pocketdictionary.view.word

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.FragmentWordBindingImpl
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.viewmodel.WordViewModel

class WordFragment : Fragment() { // todo implement properly
    companion object {
        @JvmStatic
        fun newInstance(dictionaryRecord: DictionaryRecord) = WordFragment().apply {
            arguments = Bundle().apply {
                putSerializable("record", dictionaryRecord)
            }
        }
    }

    private lateinit var dictionaryRecord: DictionaryRecord

    private lateinit var binding: FragmentWordBindingImpl
    private lateinit var viewModel: WordViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dictionaryRecord = arguments?.getSerializable("record") as DictionaryRecord ?: throw Exception()// todo show error if null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentWordBindingImpl>(inflater, R.layout.fragment_word, container, false)
        viewModel = ViewModelProviders.
            of(this, WordViewModel.Factory(dictionaryRecord))[WordViewModel::class.java]
        binding.viewModel = viewModel
        return binding.root
    }
        //inflater.inflate(R.layout.fragment_word, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding = DataBindingUtil.setContentView(this.activity as Activity, R.layout.fragment_word)


    }
}