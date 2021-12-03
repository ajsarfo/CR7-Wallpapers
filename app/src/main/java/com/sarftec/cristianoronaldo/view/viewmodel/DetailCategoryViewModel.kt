package com.sarftec.cristianoronaldo.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.usecase.category.GetCategoryWallpapers
import com.sarftec.cristianoronaldo.domain.usecase.favorite.RemoveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.favorite.SaveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.WallpaperUIMapper
import com.sarftec.cristianoronaldo.view.parcel.CategoryToDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailCategoryViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    mapper: WallpaperUIMapper,
    getImage: GetImage,
    saveFavorite: SaveFavoriteWallpaper,
    removeFavorite: RemoveFavoriteWallpaper,
    private val getCategoryWallpapers: GetCategoryWallpapers
) : DetailBaseViewModel<CategoryToDetail>(stateHandle, getImage, saveFavorite, removeFavorite, mapper) {

    override suspend fun getWallpaperFlow(parcel: CategoryToDetail): Resource<Flow<PagingData<CR7Wallpaper>>> {
        return getCategoryWallpapers.execute(
            GetCategoryWallpapers.CategoryWallpaperParam(parcel.id)
        ).flow
    }
}
