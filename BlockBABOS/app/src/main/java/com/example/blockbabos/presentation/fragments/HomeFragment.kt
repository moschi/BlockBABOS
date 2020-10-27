package com.example.blockbabos.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blockbabos.R
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val toolbar = activity?.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.welcome_title)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}