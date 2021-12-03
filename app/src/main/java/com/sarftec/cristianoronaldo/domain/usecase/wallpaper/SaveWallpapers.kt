package com.sarftec.cristianoronaldo.domain.usecase.wallpaper

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.StorageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class SaveWallpapers @Inject constructor(
    private val storageRepository: StorageRepository
) : UseCase<SaveWallpapers.SaveParam, SaveWallpapers.SaveResult>() {

    override suspend fun execute(param: SaveParam?): SaveResult {
        if (param == null) return SaveResult(Resource.error("Save wallpaper param NULL"))
        val storageOption = when (param.option) {
            SaveOption.FAVORITE -> StorageRepository.Option.FAVORITE
            else -> StorageRepository.Option.NOT_FAVORITE
        }
        return SaveResult(
            storageRepository.saveWallpapers(storageOption, param.wallpapers)
        )
    }

    class SaveParam(val option: SaveOption, val wallpapers: List<CR7Wallpaper>) : Param
    class SaveResult(val result: Resource<Unit>) : Response

    enum class SaveOption {
        FAVORITE, NOT_FAVORITE
    }
}