package com.artyomefimov.pocketdictionary.view

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.databinding.ActivityMainBindingImpl
import com.artyomefimov.pocketdictionary.viewmodel.WordFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        requestPermissions(permissions, 123) // todo correctly request permission(only once)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val s = ""
//        }
    }
}
