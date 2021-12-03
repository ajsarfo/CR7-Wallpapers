package com.sarftec.cristianoronaldo.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.usecase.favorite.RemoveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.favorite.SaveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.domain.usecase.wallpaper.GetWallpapers
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.WallpaperUIMapper
import com.sarftec.cristianoronaldo.view.parcel.WallpaperToDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailWallpaperViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    mapper: WallpaperUIMapper,
    getImage: GetImage,
    saveFavorite: SaveFavoriteWallpaper,
    removeFavorite: RemoveFavoriteWallpaper,
    private val getWallpapers: GetWallpapers
) : DetailBaseViewModel<WallpaperToDetail>(stateHandle, getImage, saveFavorite, removeFavorite, mapper) {

    override suspend fun getWallpaperFlow(parcel: WallpaperToDetail): Resource<Flow<PagingData<CR7Wallpaper>>> {
        return getWallpapers.execute(getSelection(parcel)).wallpapers
    }

    private fun getSelection(parcel: WallpaperToDetail): GetWallpapers.Selection {
        return when (parcel.selection) {
            WallpaperToDetail.RECENT -> GetWallpapers.Selection.Recent(parcel.wallpaperId)
            WallpaperToDetail.POPULAR -> GetWallpapers.Selection.Popular(parcel.wallpaperId)
            else -> GetWallpapers.Selection.Favorite(parcel.wallpaperId)
        }
    }
}