package com.sarftec.cristianoronaldo.domain.usecase.favorite

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteWallpapers @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<GetFavoriteWallpapers.EmptyParam, GetFavoriteWallpapers.GetFavoriteResult>() {

    override suspend fun execute(param: EmptyParam?): GetFavoriteResult {
        val result = wallpaperRepository.getWallpapersForSelection(
            WallpaperRepository.RepoSection.Favorite()
        )
        return GetFavoriteResult(result)
    }

    object EmptyParam : Param
    class GetFavoriteResult(
        val resource: Resource<Flow<PagingData<CR7Wallpaper>>>
    ) : Response
}