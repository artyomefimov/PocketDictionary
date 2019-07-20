package com.artyomefimov.pocketdictionary.view

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
