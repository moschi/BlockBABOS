package com.example.blockbabos.presentation.fragments

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.moviedbapi.ApiController

class SwipeViewModelFactory(
    private val dataSource: BaboMovieDao, private val apiController: ApiController, private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwipeViewModel::class.java)) {
            return SwipeViewModel(dataSource, apiController, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}