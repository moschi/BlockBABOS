package com.example.blockbabos.persistence

import androidx.test.core.app.ApplicationProvider
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResult
import org.junit.*


class BaboMovieRoomDatabaseTest {
    private var baboMovieDao: BaboMovieDao? = null
    private var db: BaboMovieRoomDatabase? = null

    @Before
    fun setup() {
        BaboMovieRoomDatabase.TEST_MODE = true
        db =
            BaboMovieRoomDatabase.getDatabase(ApplicationProvider.getApplicationContext())
        baboMovieDao = db!!.baboMovieDao()
    }


    @Test
    fun insertAndDelete() {
        val newMovie = BaboMovie()
        newMovie.movieDbApiId = 1
        newMovie.title = "newMovie"
        newMovie.result = SwipeResult.LIKED
        baboMovieDao?.insert(newMovie)

        Assert.assertEquals(1, baboMovieDao?.getEntriesAsList()?.size)
        Assert.assertEquals(1, baboMovieDao?.getLikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getDislikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getSuperlikedAsList()?.size)

        baboMovieDao?.delete(newMovie)

        Assert.assertEquals(0, baboMovieDao?.getEntriesAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getLikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getDislikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getSuperlikedAsList()?.size)
    }


    @Test
    fun multiInsertAndDelete() {
        val newMovie1 = BaboMovie()
        newMovie1.movieDbApiId = 1
        newMovie1.title = "newMovie1"
        newMovie1.result = SwipeResult.DISLIKED;
        baboMovieDao?.insert(newMovie1)

        val newMovie2 = BaboMovie()
        newMovie2.movieDbApiId = 2
        newMovie2.title = "newMovie2"
        newMovie2.result = SwipeResult.LIKED;
        baboMovieDao?.insert(newMovie2)

        val newMovie3 = BaboMovie()
        newMovie3.movieDbApiId = 3
        newMovie3.title = "newMovie3"
        newMovie3.result = SwipeResult.SUPERLIKED;
        baboMovieDao?.insert(newMovie3)

        Assert.assertEquals(3, baboMovieDao?.getEntriesAsList()?.size)
        Assert.assertEquals(1, baboMovieDao?.getLikedAsList()?.size)
        Assert.assertEquals(1, baboMovieDao?.getDislikedAsList()?.size)
        Assert.assertEquals(1, baboMovieDao?.getSuperlikedAsList()?.size)

        baboMovieDao?.deleteAll()

        Assert.assertEquals(0, baboMovieDao?.getEntriesAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getLikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getDislikedAsList()?.size)
        Assert.assertEquals(0, baboMovieDao?.getSuperlikedAsList()?.size)
    }

    @Test
    fun conflictStrategyReplace() {
        val newMovie1 = BaboMovie()
        newMovie1.movieDbApiId = 1
        newMovie1.title = "newMovie1"
        newMovie1.result = SwipeResult.SUPERLIKED
        baboMovieDao?.insert(newMovie1)

        val newMovie2 = BaboMovie()
        newMovie2.movieDbApiId = 1
        newMovie2.title = "newMovie2"
        newMovie2.result = SwipeResult.SUPERLIKED
        baboMovieDao?.insert(newMovie2)

        Assert.assertEquals(1, baboMovieDao?.getEntriesAsList()?.size)
        // entry gets deleted by primary key
        baboMovieDao?.delete(newMovie1)
        Assert.assertEquals(0, baboMovieDao?.getEntriesAsList()?.size)
    }
}