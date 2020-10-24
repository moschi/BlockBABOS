package com.example.blockbabos.presentation

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.helper.Swipe
import com.example.blockbabos.presentation.fragments.FragmentStates
import com.example.blockbabos.presentation.fragments.ListFragment
import com.example.blockbabos.presentation.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var videoFragment: VideoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val mgr: FragmentManager = supportFragmentManager

        if (savedInstanceState != null) {
            val frag = supportFragmentManager.getFragment(
                savedInstanceState,
                FragmentStates.VIDEO_FRAGMENT.name
            )
            if (frag != null) {
                videoFragment = frag as VideoFragment
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    renderVideoFragment(mgr)
                    true
                }
                R.id.nav_lists -> {
                    renderListFragment(mgr)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(
            outState,
            FragmentStates.VIDEO_FRAGMENT.name,
            videoFragment
        )
    }

    private fun renderVideoFragment(mgr: FragmentManager) {
        videoFragment = VideoFragment.newInstance()
        val trans: FragmentTransaction = mgr.beginTransaction()
        trans.replace(R.id.main_fragment_container, videoFragment)
        trans.commit()
    }

    private fun renderListFragment(mgr: FragmentManager) {
        val listFragment: Fragment = ListFragment.newInstance(1)
        val trans: FragmentTransaction = mgr.beginTransaction()
        listFragment.retainInstance = true
        trans.replace(R.id.main_fragment_container, listFragment)
        trans.commit()
    }

    fun onSwipe(type: Swipe.SwipeType) {
        videoFragment.onSwipe(type)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}