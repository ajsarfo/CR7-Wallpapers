package com.sarftec.cristianoronaldo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.domain.usecase.wallpaper.GetWallpapers
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.WallpaperUIMapper
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpapersViewModel @Inject constructor(
     val wallpaperUIMapper: WallpaperUIMapper,
     val getWallpapers: GetWallpapers,
    getImage: GetImage,
) : BaseViewModel<WallpaperUI.Wallpaper>(getImage) {

    private val _wallpaperFlow = MutableLiveData<Resource<Flow<PagingData<WallpaperUI>>>>()
    val wallpaperFlow: LiveData<Resource<Flow<PagingData<WallpaperUI>>>>
        get() = _wallpaperFlow

    fun loadWallpapers(selection: Selection) {
        _wallpaperFlow.value = Resource.loading()
        val selectionType =  when(selection) {
            Selection.POPULAR -> GetWallpapers.Selection.Popular()
            Selection.FAVORITE -> GetWallpapers.Selection.Favorite()
            Selection.RECENT -> GetWallpapers.Selection.Recent()
        }
        viewModelScope.launch {
            getWallpapers.execute(selectionType).let {
                _wallpaperFlow.value =
                    if (it.wallpapers.isSuccess()) it.wallpapers.data?.let { flow ->
                        mapFlowToViewUI(flow)
                    }
                    else Resource.error(it.wallpapers.message)
            }
        }
    }

    private fun mapFlowToViewUI(flow: Flow<PagingData<CR7Wallpaper>>): Resource<Flow<PagingData<WallpaperUI>>> {
        return Resource.success(
            flow.map { pagingData ->
                pagingData.map { wallpaper ->
                    wallpaperUIMapper.mapToViewUI(wallpaper)
                }
            }
        )
    }

    enum class Selection {
        RECENT, POPULAR, FAVORITE
    }
}