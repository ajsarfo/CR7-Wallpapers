package com.sarftec.cristianoronaldo.domain.usecase.category

import com.sarftec.cristianoronaldo.domain.model.CR7Category
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class GetCategories @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<GetCategories.EmptyParam, GetCategories.CategoryResult>() {

    object EmptyParam : Param

    class CategoryResult(val categories: Resource<List<CR7Category>>) : Response

    override suspend fun execute(param: EmptyParam?): CategoryResult {
        val resource = wallpaperRepository.getWallpaperCategories()
        if (resource.isError()) Resource.error("${resource.message}")
        else resource.data
            ?.let { Resource.success(it) }
            ?: Resource.error("Error => empty categories")
        return CategoryResult(resource)
    }
}