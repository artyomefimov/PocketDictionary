package com.artyomefimov.pocketdictionary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import com.artyomefimov.pocketdictionary.R
import com.artyomefimov.pocketdictionary.view.wordlist.WordListFragment

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener(this)

        var listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (listFragment == null) {
            listFragment = WordListFragment.newInstance()
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, listFragment)
                commit()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onBackStackChanged() {
        isShouldDisplayHomeUp()
    }

    private fun isShouldDisplayHomeUp() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(canGoBack)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return false
    }
}
