package com.sarftec.cristianoronaldo.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.usecase.favorite.RemoveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.favorite.SaveFavoriteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.WallpaperUIMapper
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class DetailBaseViewModel<T : Parcelable>(
    private val stateHandle: SavedStateHandle,
    getImage: GetImage,
    private val saveFavorite: SaveFavoriteWallpaper,
    private val removeFavorite: RemoveFavoriteWallpaper,
    private val mapper: WallpaperUIMapper
) : BaseViewModel<WallpaperUI.Wallpaper>(getImage) {

    private val _wallpaperFlow = MutableLiveData<Resource<Flow<PagingData<WallpaperUI>>>>()
    val wallpaperFlow: LiveData<Resource<Flow<PagingData<WallpaperUI>>>>
        get() = _wallpaperFlow

    private val cacheImageMap = hashMapOf<Int, String>()

    protected abstract suspend fun getWallpaperFlow(parcel: T): Resource<Flow<PagingData<CR7Wallpaper>>>

    fun loadWallpaperFlow() {
        _wallpaperFlow.value = Resource.loading()
        val parcel = stateHandle.get<T>(PARCEL) ?: let {
            _wallpaperFlow.value = Resource.error("Error => parcel not found in state handle!")
            return
        }
        viewModelScope.launch {
            _wallpaperFlow.value = getWallpaperFlow(parcel).let {
                if (it.isSuccess()) mapFlowToViewUI(it.data!!)
                else Resource.error("${it.message}")
            }
        }
    }

    fun saveFavoriteWallpaper(wallpaperUI: WallpaperUI.Wallpaper) {
        viewModelScope.launch {
            saveFavorite.execute(
                mapper.mapToCR7Wallpaper(wallpaperUI).let {
                    SaveFavoriteWallpaper.SaveFavoriteParam(it)
                }
            )
        }
    }

    fun removeFavoriteWallpaper(wallpaperUI: WallpaperUI.Wallpaper) {
        viewModelScope.launch {
            removeFavorite.execute(
                mapper.mapToCR7Wallpaper(wallpaperUI).let {
                    RemoveFavoriteWallpaper.RemoveParam(it)
                }
            )
        }
    }

    fun setParcel(parcel: T) {
        stateHandle.set(PARCEL, parcel)
    }

    private fun mapFlowToViewUI(flow: Flow<PagingData<CR7Wallpaper>>): Resource<Flow<PagingData<WallpaperUI>>> {
        return Resource.success(
            flow.map { pagingData ->
                pagingData.map { wallpaper ->
                    mapper.mapToViewUI(wallpaper)
                }
            }
        )
    }

    companion object {
        const val PARCEL = "parcel"
    }
}