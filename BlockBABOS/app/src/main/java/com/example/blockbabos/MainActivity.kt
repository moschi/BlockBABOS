package com.example.blockbabos

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import com.example.blockbabos.listeners.Swipe
import com.example.blockbabos.model.BaboMovie
import com.example.blockbabos.model.SwipeResult
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var videoFragment: VideoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val mgr: FragmentManager = supportFragmentManager
        //TODO videoFragment not initialized when "onCreate" because of orientation change
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    videoFragment = VideoFragment.newInstance("bla", "bla")
                    val trans: FragmentTransaction = mgr.beginTransaction()
                    trans.replace(R.id.main_fragment_container, videoFragment)
                    trans.commit()

                    true
                }
                R.id.nav_lists -> {
                    val listFragment: Fragment = ListFragment.newInstance("bla", "bli")
                    val trans: FragmentTransaction = mgr.beginTransaction()
                    trans.replace(R.id.main_fragment_container, listFragment)
                    trans.commit()
                    true
                }
                else -> false
            }
        }
    }

    fun onSwipe(type: Swipe.SwipeType) {
        //TODO if above fixed, RemoveMe
        if(this::videoFragment.isInitialized){
            videoFragment.onSwipe(type)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}