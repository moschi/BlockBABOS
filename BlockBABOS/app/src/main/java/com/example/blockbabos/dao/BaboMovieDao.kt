package com.example.blockbabos.dao

import androidx.room.*
import com.example.blockbabos.model.BaboMovie

@Dao
interface BaboMovieDao {

    @Query("SELECT * FROM BaboMovie ORDER BY movieDbApiId DESC")
    fun getEntries(): List<BaboMovie>

    @Query("SELECT * FROM BaboMovie WHERE result = 0 ORDER BY movieDbApiId DESC")
    fun getLiked(): List<BaboMovie>

    @Query("SELECT * FROM BaboMovie WHERE result = 1 ORDER BY movieDbApiId DESC")
    fun getDisliked(): List<BaboMovie>

    @Query("SELECT * FROM BaboMovie WHERE result = 2 ORDER BY movieDbApiId DESC")
    fun getSuperliked(): List<BaboMovie>

    @Insert
    fun insert(entry: BaboMovie)

    @Delete
    fun delete(entry: BaboMovie)

    @Update
    fun update(entry: BaboMovie)

    @Query("DELETE FROM BaboMovie")
    fun deleteAll()
}