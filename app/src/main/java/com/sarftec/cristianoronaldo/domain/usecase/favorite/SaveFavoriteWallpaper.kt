package com.sarftec.cristianoronaldo.domain.usecase.favorite

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.StorageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class SaveFavoriteWallpaper @Inject constructor(
    private val storageRepository: StorageRepository
) : UseCase<SaveFavoriteWallpaper.SaveFavoriteParam, SaveFavoriteWallpaper.SaveFavoriteResult>() {

    class SaveFavoriteParam(val param: CR7Wallpaper) : Param
    class SaveFavoriteResult(val result: Resource<Unit>) : Response

    override suspend fun execute(param: SaveFavoriteParam?): SaveFavoriteResult {
        val result = if (param == null) Resource.error("Save favorite is NULL!")
        else storageRepository.saveWallpaper(
            StorageRepository.Option.FAVORITE,
            param.param
        )
        return SaveFavoriteResult(result)
    }
}