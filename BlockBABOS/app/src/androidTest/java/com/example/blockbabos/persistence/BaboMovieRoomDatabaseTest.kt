package com.example.blockbabos.persistence

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResult
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws


@RunWith(AndroidJUnit4::class)
class BaboMovieRoomDatabaseTest {


    private var baboMovieDao: BaboMovieDao? = null
    private var db: BaboMovieRoomDatabase? = null

    @Before
    fun onCreateDB() {
        BaboMovieRoomDatabase.TEST_MODE = true
        db = BaboMovieRoomDatabase.getDatabase(ApplicationProvider.getApplicationContext())
        baboMovieDao = db!!.baboMovieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db!!.close()
    }

    @Test
    fun simpleInsert() {
        /*
        val newMovie = BaboMovie()
        newMovie.movieDbApiId = 1
        newMovie.title = "newlyInsertedMovie"
        newMovie.result = SwipeResult.LIKED
        baboMovieDao?.insert(newMovie)

        val entries: List<BaboMovie> = baboMovieDao!!.getEntries().value!!
        for (entry in entries) {
            Log.d(null, entry.movieDbApiId.toString() + " | " + entry.title + " | " + entry.result)
        }


        Assert.assertEquals(1, baboMovieDao?.getEntries()?.value?.size)
        Assert.assertEquals(1, baboMovieDao?.getLiked()?.value?.size)
        Assert.assertEquals(0, baboMovieDao?.getDisliked()?.value?.size)
        Assert.assertEquals(0, baboMovieDao?.getSuperliked()?.value?.size)

        baboMovieDao?.delete(newMovie)

        Assert.assertEquals(0, baboMovieDao?.getEntries()?.value?.size)
        Assert.assertEquals(0, baboMovieDao?.getLiked()?.value?.size)
        Assert.assertEquals(0, baboMovieDao?.getDisliked()?.value?.size)
        Assert.assertEquals(0, baboMovieDao?.getSuperliked()?.value?.size)
        */
    }
}