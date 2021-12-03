package com.sarftec.cristianoronaldo.domain.usecase.category

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWallpapers @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<GetCategoryWallpapers.CategoryWallpaperParam, GetCategoryWallpapers.CategoryWallpaperResult>() {

    override suspend fun execute(param: CategoryWallpaperParam?): CategoryWallpaperResult {
        val result = if (param == null)  Resource.error("Error => GetCategoryWallpapers NULL param!")
         else wallpaperRepository.getWallpapersForCategory(param.id)
        return CategoryWallpaperResult(result)
    }

    class CategoryWallpaperParam(val id: String) : Param
    class CategoryWallpaperResult(val flow: Resource<Flow<PagingData<CR7Wallpaper>>>) : Response
}