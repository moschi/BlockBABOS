package com.example.blockbabos.model

import androidx.room.TypeConverter

enum class SwipeResult {
    LIKED,
    DISLIKED,
    SUPERLIKED,
    UNKNOWN
}

class SwipeResultConverter{

    @TypeConverter
    fun fromHealth(value: SwipeResult): Int{
        return value.ordinal
    }

    @TypeConverter
    fun toHealth(value: Int): SwipeResult{
        return when(value){
            0 -> SwipeResult.LIKED
            1 -> SwipeResult.DISLIKED
            2 -> SwipeResult.SUPERLIKED
            else -> SwipeResult.UNKNOWN
        }
    }

}