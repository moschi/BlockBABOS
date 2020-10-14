package com.example.blockbabos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
class BaboMovie {
    @PrimaryKey
    var movieDbApiId: Int = 0

    @ColumnInfo
    lateinit var title: String

    @ColumnInfo
    @TypeConverters(SwipeResultConverter::class)
    lateinit var result: SwipeResult
}