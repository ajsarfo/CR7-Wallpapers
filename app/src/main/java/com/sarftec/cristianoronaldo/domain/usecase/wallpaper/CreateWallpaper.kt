package com.sarftec.cristianoronaldo.domain.usecase.wallpaper

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class CreateWallpaper @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<CreateWallpaper.CreateParam, CreateWallpaper.CreateResult>() {

    override suspend fun execute(param: CreateParam?): CreateResult {
        val result = param?.let {
            val section = when(it.section) {
                CreateSection.RECENT -> WallpaperRepository.RepoSection.Recent()
                CreateSection.POPULAR -> WallpaperRepository.RepoSection.Popular()
            }
            wallpaperRepository.createWallpaper(section, it.imageInfo, it.categoryId)
        } ?: Resource.error("Create wallpaper null param!")
        return CreateResult(result)
    }

    class CreateParam(
        val categoryId: String,
        val imageInfo: ImageInfo,
        val section: CreateSection
    ) : Param

    class CreateResult(
        val result: Resource<CR7Wallpaper>
    ) : Response

    enum class CreateSection {
        RECENT , POPULAR
    }

}