package com.artyomefimov.pocketdictionary

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.artyomefimov.pocketdictionary.databinding.ActivityMainBindingImpl
import com.artyomefimov.pocketdictionary.viewmodel.TranslationViewModel
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

        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        requestPermissions(permissions, 123)

        text.setOnClickListener {
            //viewModel.loadTranslation("History dispatcher", "en|ru")
//            viewModel.writeTranslations()
//            val translation = viewModel.getTrans("word")
//            text.text = translation.translations.toString()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val s = ""
//        }
    }
}
