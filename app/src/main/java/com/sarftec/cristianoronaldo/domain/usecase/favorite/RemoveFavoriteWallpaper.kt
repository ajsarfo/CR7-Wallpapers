package com.sarftec.cristianoronaldo.domain.usecase.favorite

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.StorageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class RemoveFavoriteWallpaper @Inject constructor(
    private val storageRepository: StorageRepository
) : UseCase<RemoveFavoriteWallpaper.RemoveParam, RemoveFavoriteWallpaper.RemoveResult>() {

    override suspend fun execute(param: RemoveParam?): RemoveResult {
        if (param == null) return RemoveResult(Resource.error("Not yet implemented!"))
        return storageRepository.removeWallpaper(StorageRepository.Option.FAVORITE, param.param)
            .let {
                if (it.isSuccess()) RemoveResult(it)
                else RemoveResult(Resource.error("${it.message}"))
            }
    }

    class RemoveParam(val param: CR7Wallpaper) : Param
    class RemoveResult(val result: Resource<Unit>) : Response
}