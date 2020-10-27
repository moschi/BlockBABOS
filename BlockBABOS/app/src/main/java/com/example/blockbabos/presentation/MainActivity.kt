package com.example.blockbabos.presentation

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val CHANNEL_ID = "BlockBABO"

class MainActivity : AppCompatActivity() {
    private lateinit var videoFragment: VideoFragment
    private lateinit var currentFragment: Fragment
    private lateinit var fragmentState: FragmentStates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.menu.setGroupCheckable(0, false, true)
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
            bottomNavigation.menu.setGroupCheckable(0, true, true)
            when (item.itemId) {
                R.id.nav_home -> {
                    fragmentState = FragmentStates.HOME_FRAGMENT
                    renderHomeFragment(mgr)
                    true
                }
                R.id.nav_swipe -> {
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
        scheduleAlarm()
        createNotificationChannel()
    }

    private fun scheduleAlarm() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 45)
        calendar.set(Calendar.SECOND, 0)

        val notificationReminder = Intent(this, ReminderNotification::class.java)
        notificationReminder.putExtra("text", getString(R.string.time_for_movie))
        val pi = PendingIntent.getBroadcast(
            this,
            0,
            notificationReminder,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        Log.d(CHANNEL_ID, "alarm set at: ${df.format(calendar.time)}")
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BlockBABO"
            val descriptionText = "BlockBABO messages"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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
        if (this::videoFragment.isInitialized) {
            videoFragment.onSwipe(type)
        } else if (this::currentFragment.isInitialized) {
            videoFragment = currentFragment as VideoFragment
            onSwipe(type)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}