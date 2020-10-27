package com.example.blockbabos.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.blockbabos.R
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireNotNull(activity)
        val toolbar = activity.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.welcome_title)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireNotNull(activity)
        val recommendMeButton: Button = activity.findViewById(R.id.recommend_me_button)
        val recommendationTextField: TextView = activity.findViewById(R.id.recommendation_text)
        recommendMeButton.setOnClickListener {
            val application = requireNotNull(this.activity).application
            val watchList =
                BaboMovieRoomDatabase.getDatabase(application).baboMovieDao().getSuperlikedAsList()
            val watchListCount = watchList.size
            val randomIndex = (Math.random() * watchListCount).toInt()
            val randomMovie = watchList[randomIndex]
            recommendationTextField.text = randomMovie.title
        }
    }
}