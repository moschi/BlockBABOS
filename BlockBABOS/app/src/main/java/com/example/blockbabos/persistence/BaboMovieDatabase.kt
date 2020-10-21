package com.example.blockbabos.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.blockbabos.dao.BaboMovieDao
import com.example.blockbabos.model.BaboMovie
import com.example.blockbabos.model.SwipeResultConverter

@Database(entities = [BaboMovie::class], version = 1, exportSchema = false)
@TypeConverters(SwipeResultConverter::class)
abstract class BaboMovieRoomDatabase : RoomDatabase() {

    abstract fun baboMovieDao(): BaboMovieDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BaboMovieRoomDatabase? = null

        var TEST_MODE = false
        private val databaseName = "babomovie_database"

        fun getDatabase(context: Context): BaboMovieRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                if (TEST_MODE) {
                    val instance = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        BaboMovieRoomDatabase::class.java
                    ).allowMainThreadQueries().build()
                    INSTANCE = instance
                    return instance
                } else {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BaboMovieRoomDatabase::class.java,
                        databaseName
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }

}