package com.example.blockbabos.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.model.SwipeResultConverter

@Database(entities = [BaboMovie::class], version = 1, exportSchema = false)
@TypeConverters(SwipeResultConverter::class)
abstract class BaboMovieRoomDatabase : RoomDatabase() {
    abstract fun baboMovieDao(): BaboMovieDao
    companion object {
        @Volatile
        private var INSTANCE: BaboMovieRoomDatabase? = null

        var TEST_MODE = false
        private const val DATABASE_NAME = "babomovie_database"

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
                        DATABASE_NAME
                    ).allowMainThreadQueries().build()
                    // todo: allowMainThreadQueries needs to be removed as it is not recommended (except for tests)
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }

}