package com.sarftec.cristianoronaldo.data.repository

import com.sarftec.cristianoronaldo.data.room.CR7Database
import com.sarftec.cristianoronaldo.data.room.mapper.RoomFavoriteMapper
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.StorageRepository
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val appDatabase: CR7Database,
    private val mapper: RoomFavoriteMapper
) : StorageRepository {

    override suspend fun saveWallpapers(
        option: StorageRepository.Option,
        wallpapers: List<CR7Wallpaper>
    ): Resource<Unit> {
        return Resource.error("Not yet implemented for => $option")
    }

    override suspend fun saveWallpaper(
        option: StorageRepository.Option,
        wallpaper: CR7Wallpaper
    ): Resource<Unit> {
        if (option == StorageRepository.Option.NOT_FAVORITE) {
            return Resource.error("Not yet implemented for => $option")
        }
        appDatabase.roomFavoriteWallpaperDao()
            .insertWallpaper(mapper.toRoomFavoriteWallpaper(wallpaper))
        return Resource.success(Unit)
    }

    override suspend fun removeWallpapers(option: StorageRepository.Option): Resource<Unit> {
        return Resource.error("Not implemented yet!")
    }

    override suspend fun removeWallpaper(
        option: StorageRepository.Option,
        wallpaper: CR7Wallpaper
    ): Resource<Unit> {
        if(option == StorageRepository.Option.NOT_FAVORITE) {
            return Resource.error("Not implemented yet!")
        }
        appDatabase.roomFavoriteWallpaperDao().removeWallpaper(
            wallpaper.id.toString()
        )
        return Resource.success(Unit)
    }
}