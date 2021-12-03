package com.sarftec.cristianoronaldo.data.room.dao

import androidx.room.*
import com.sarftec.cristianoronaldo.data.ROOM_IMAGE_TABLE
import com.sarftec.cristianoronaldo.data.room.entity.RoomImage

@Dao
interface RoomImageDao {

    @Query("select * from $ROOM_IMAGE_TABLE where id = :id")
    suspend fun getRoomImage(id: String) : RoomImage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomImage(image: RoomImage)

    @Query("delete from $ROOM_IMAGE_TABLE")
    suspend fun deleteAllRoomImages()

    @Query("delete from $ROOM_IMAGE_TABLE where id = :id")
    suspend fun deleteRoomImage(id: String)
}