package com.sarftec.cristianoronaldo.domain.usecase.wallpaper

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class DeleteWallpaper @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<DeleteWallpaper.DeleteParam, DeleteWallpaper.DeleteResult>() {

    override suspend fun execute(param: DeleteParam?): DeleteResult? {
        val result = param?.let {
            wallpaperRepository.deleteWallpaper(it.param)
        } ?: Resource.error("Delete wallpaper param is null!")
        return DeleteResult(result)
    }

    class DeleteParam(val param: CR7Wallpaper) : Param
    class DeleteResult(val result: Resource<Unit>) : Response
}