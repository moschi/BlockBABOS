package com.example.blockbabos.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import javax.sql.CommonDataSource

class ListViewModel (
    dataSource: BaboMovieDao,
    application: Application
) : ViewModel() {

    val dao = dataSource
    val superLiked = dao.getSuperliked()

}