package com.sarftec.cristianoronaldo.data.room.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sarftec.cristianoronaldo.data.ROOM_IMAGE_TABLE

@Entity(tableName = ROOM_IMAGE_TABLE)
class RoomImage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val uri: Uri
)