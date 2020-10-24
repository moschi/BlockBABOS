package com.example.blockbabos.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.blockbabos.domain.model.BaboMovie

@Dao
interface BaboMovieDao {

    @Query("SELECT * FROM BaboMovie ORDER BY movieDbApiId DESC")
    fun getEntries(): LiveData<List<BaboMovie>>

    @Query("SELECT * FROM BaboMovie WHERE result = 0 ORDER BY movieDbApiId DESC")
    fun getLiked(): LiveData<List<BaboMovie>>

    @Query("SELECT * FROM BaboMovie WHERE result = 1 ORDER BY movieDbApiId DESC")
    fun getDisliked(): LiveData<List<BaboMovie>>

    @Query("SELECT * FROM BaboMovie WHERE result = 2 ORDER BY movieDbApiId DESC")
    fun getSuperliked(): LiveData<List<BaboMovie>>

    @Query("SELECT * from BaboMovie WHERE movieDbApiId = :key")
    suspend fun get(key: Int): BaboMovie?

    @Insert
    fun insert(entry: BaboMovie)

    @Delete
    fun delete(entry: BaboMovie)

    @Update
    fun update(entry: BaboMovie)

    @Query("DELETE FROM BaboMovie")
    fun deleteAll()
}