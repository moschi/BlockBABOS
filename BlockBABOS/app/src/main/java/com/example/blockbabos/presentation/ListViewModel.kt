package com.example.blockbabos.presentation

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResult
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import javax.sql.CommonDataSource

class ListViewModel (
    dataSource: BaboMovieDao,
    application: Application
) : ViewModel() {

    val dao = dataSource
    var superLiked = dao.getSuperliked()

    fun onDelete(movie: BaboMovie) {
        movie.result = SwipeResult.LIKED
        dao.update(movie)
        this.superLiked = dao.getSuperliked()
    }
}