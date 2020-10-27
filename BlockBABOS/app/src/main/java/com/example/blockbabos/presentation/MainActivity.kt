package com.example.blockbabos.presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.helper.Swipe
import com.example.blockbabos.presentation.fragments.FragmentStates
import com.example.blockbabos.presentation.fragments.HomeFragment
import com.example.blockbabos.presentation.fragments.ListFragment
import com.example.blockbabos.presentation.fragments.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_babo_movie_list.*


class MainActivity : AppCompatActivity() {
    private lateinit var videoFragment: VideoFragment
    private lateinit var currentFragment: Fragment
    private lateinit var fragmentState: FragmentStates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.menu.setGroupCheckable(0, false, true);
        val mgr: FragmentManager = supportFragmentManager
        if (savedInstanceState != null) {
            val fragmentStatesValue = savedInstanceState.getString("fragmentState") ?: FragmentStates.HOME_FRAGMENT.name
            fragmentState =  FragmentStates.valueOf(fragmentStatesValue)
            val frag = supportFragmentManager.getFragment(
                savedInstanceState,
                fragmentState.name
            )
            if (frag != null) {
                renderFragment(mgr, frag)
            }
        } else {
            fragmentState = FragmentStates.HOME_FRAGMENT
            renderHomeFragment(mgr)
        }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            bottomNavigation.menu.setGroupCheckable(0, true, true);
            when (item.itemId) {
                R.id.nav_home -> {
                    fragmentState = FragmentStates.VIDEO_FRAGMENT
                    renderVideoFragment(mgr)
                    true
                }
                R.id.nav_lists -> {
                    fragmentState = FragmentStates.LIST_FRAGMENT
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
            fragmentState.name,
            currentFragment
        )
        outState.putString("fragmentState", fragmentState.name)
    }

    private fun renderHomeFragment(mgr: FragmentManager) {
        val homeFragment = HomeFragment.newInstance()
        renderFragment(mgr, homeFragment)
    }

    private fun renderVideoFragment(mgr: FragmentManager) {
        videoFragment = VideoFragment.newInstance()
        renderFragment(mgr, videoFragment)
    }

    private fun renderListFragment(mgr: FragmentManager) {
        val listFragment: Fragment = ListFragment.newInstance()
        renderFragment(mgr, listFragment)
    }

    private fun renderFragment(mgr: FragmentManager, fragment: Fragment) {
        val trans: FragmentTransaction = mgr.beginTransaction()
        // fragment.retainInstance = true
        trans.replace(R.id.main_fragment_container, fragment)
        trans.commit()
        currentFragment = fragment
    }

    fun onSwipe(type: Swipe.SwipeType) {
        if(this::videoFragment.isInitialized){
            videoFragment.onSwipe(type)
        }else if(this::currentFragment.isInitialized){
            videoFragment = currentFragment as VideoFragment
            onSwipe(type)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}