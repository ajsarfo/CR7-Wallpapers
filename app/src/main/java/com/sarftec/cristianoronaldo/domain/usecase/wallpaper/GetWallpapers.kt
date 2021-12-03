package com.sarftec.cristianoronaldo.domain.usecase.wallpaper

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWallpapers @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<GetWallpapers.Selection, GetWallpapers.WallpaperResult>() {

    override suspend fun execute(param: Selection?): WallpaperResult {
        val selection = param ?: return WallpaperResult(Resource.error("Get wallpaper param => NULL!"))
        val pagingSource = when (selection) {
            is Selection.Recent -> wallpaperRepository.getWallpapersForSelection(
                WallpaperRepository.RepoSection.Recent(
                    selection.position
                )
            )
            is Selection.Favorite -> wallpaperRepository.getWallpapersForSelection(
                WallpaperRepository.RepoSection.Favorite(
                    selection.position
                )
            )
            is Selection.Popular -> wallpaperRepository.getWallpapersForSelection(
                WallpaperRepository.RepoSection.Popular(
                    selection.position
                )
            )
        }

        return WallpaperResult(
            if (pagingSource.isError() || pagingSource.isLoading()) Resource.error("Failed to get wallpaper! => ${pagingSource.message}")
            else pagingSource
        )
    }

    sealed class Selection(val position: Long) : Param {
        class Recent(position: Long = WallpaperRepository.INITIAL_SECTION) : Selection(position)
        class Popular(position: Long = WallpaperRepository.INITIAL_SECTION) : Selection(position)
        class Favorite(position: Long = WallpaperRepository.INITIAL_SECTION) : Selection(position)
    }

    class WallpaperResult(
        val wallpapers: Resource<Flow<PagingData<CR7Wallpaper>>>
    ) : Response
}