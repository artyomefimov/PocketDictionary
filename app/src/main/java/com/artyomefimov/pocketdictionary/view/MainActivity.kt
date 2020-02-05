package com.artyomefimov.pocketdictionary.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.artyomefimov.pocketdictionary.R

class MainActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

//        appBarConfiguration = AppBarConfiguration.Builder().build()
//        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment), appBarConfiguration) // todo не работает, jvm 1.6 -> 1.8
    }

    private fun isShouldDisplayHomeUp(): Boolean { // todo
        findNavController(R.id.nav_host_fragment).apply {
            return graph.startDestination != currentDestination?.id
        }
    }

    override fun onBackPressed() {
        //supportActionBar?.setDisplayHomeAsUpEnabled(isShouldDisplayHomeUp())
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp() ||
                super.onSupportNavigateUp()
}
