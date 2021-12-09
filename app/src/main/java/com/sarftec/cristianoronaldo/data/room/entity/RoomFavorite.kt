package com.sarftec.cristianoronaldo.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarftec.cristianoronaldo.data.ROOM_FAVORITE_TABLE

@Entity(tableName = ROOM_FAVORITE_TABLE)
class RoomFavorite(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageLocation: String,
)