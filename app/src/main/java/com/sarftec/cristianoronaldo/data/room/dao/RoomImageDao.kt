package com.sarftec.cristianoronaldo.data.room.dao

import androidx.room.*
import com.sarftec.cristianoronaldo.data.ROOM_IMAGE_TABLE
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage

@Dao
interface RoomImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(roomImage: RoomImage)

    @Query("select * from $ROOM_IMAGE_TABLE where id = :id")
    suspend fun getImage(id: String) : RoomImage?

    @Query("delete from $ROOM_IMAGE_TABLE")
    suspend fun clearImages()
}