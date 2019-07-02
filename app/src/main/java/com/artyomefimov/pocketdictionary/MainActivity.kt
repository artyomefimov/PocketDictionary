package com.artyomefimov.pocketdictionary

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.artyomefimov.pocketdictionary.databinding.ActivityMainBindingImpl
import com.artyomefimov.pocketdictionary.ui.TranslationViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBindingImpl
    private lateinit var viewModel: TranslationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(TranslationViewModel::class.java)
        binding.viewModel = viewModel



        text.setOnClickListener {
            viewModel.loadTranslation("History dispatcher", "en|ru")
        }
    }
}
