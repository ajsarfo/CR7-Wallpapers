package com.sarftec.cristianoronaldo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.usecase.approve.ApproveWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.approve.DeleteWallpaper
import com.sarftec.cristianoronaldo.domain.usecase.approve.GetApproveWallpapers
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.utils.Event
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.WallpaperUIMapper
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApproveViewModel @Inject constructor(
    private val getApproveWallpapers: GetApproveWallpapers,
    private val deleteWallpaper: DeleteWallpaper,
    private val approveWallpaper: ApproveWallpaper,
    private val wallpaperUIMapper: WallpaperUIMapper,
    getImage: GetImage
) : BaseViewModel<WallpaperUI.Wallpaper>(getImage) {

    private val wallpaperStateMap = hashMapOf<String, WallpaperState>()

    private val _wallpaperFlow =
        MutableLiveData<Resource<Flow<PagingData<WallpaperUI>>>>()

    val wallpaperFlow: LiveData<Resource<Flow<PagingData<WallpaperUI>>>>
        get() = _wallpaperFlow


    private val _adapterUpdate = MutableLiveData<Event<AdapterUpdate>>()
    val adapterUpdate: LiveData<Event<AdapterUpdate>>
        get() = _adapterUpdate

    fun loadWallpapers() {
        viewModelScope.launch {
            _wallpaperFlow.value = Resource.loading()
            getApproveWallpapers.execute(GetApproveWallpapers.GetApproveParam).result.let {
                _wallpaperFlow.value = if (it.isSuccess()) mapFlowToViewUI(it.data!!)
                else Resource.error("${it.message}")
            }
        }
    }

    fun getStateForWallpaper(wallpaper: WallpaperUI.Wallpaper): WallpaperState {
        return wallpaperStateMap[wallpaper.wallpaper.id.toString()] ?: WallpaperState.UNSELECTED
    }

    fun clearWallpapers() {
        wallpaperStateMap.filter { it.value == WallpaperState.SELECTED }
            .forEach {
                wallpaperStateMap[it.key] = WallpaperState.UNSELECTED
            }
        _adapterUpdate.value = Event(AdapterUpdate.All)
    }

    fun getSelectedWallpapers(): List<CR7Wallpaper> {
        return wallpaperStateMap.filter { it.value == WallpaperState.SELECTED }
            .map {
                CR7Wallpaper(it.key.toLong(), 0, 0, "", "", "", false)
            }
    }

    fun deleteWallpaper(id: String) {
        wallpaperStateMap[id]?.let {
            wallpaperStateMap[id] = WallpaperState.DELETED
            _adapterUpdate.value = Event(
                AdapterUpdate.Single(id, WallpaperState.DELETED)
            )
        }
    }

    suspend fun approveWallpaper(wallpaper: CR7Wallpaper): Resource<Unit> {
        return approveWallpaper.execute(
            ApproveWallpaper.ApproveParam(wallpaper)
        ).result
    }

    suspend fun deleteWallpaper(wallpaper: CR7Wallpaper): Resource<Unit> {
        return deleteWallpaper.execute(
            DeleteWallpaper.DeleteParam(wallpaper)
        ).result
    }

    fun approveWallpaper(id: String) {
        wallpaperStateMap[id]?.let {
            wallpaperStateMap[id] = WallpaperState.APPROVED
            _adapterUpdate.value = Event(
                AdapterUpdate.Single(id, WallpaperState.APPROVED)
            )
        }
    }

    fun setSelectionState(wallpaper: WallpaperUI.Wallpaper) {
        val oldState = wallpaperStateMap[wallpaper.wallpaper.id.toString()]
            ?: WallpaperState.UNSELECTED
        val newState = when (oldState) {
            WallpaperState.UNSELECTED -> WallpaperState.SELECTED
            WallpaperState.SELECTED -> WallpaperState.UNSELECTED
            else -> return
        }
        wallpaperStateMap[wallpaper.wallpaper.id.toString()] = newState
        _adapterUpdate.value = Event(
            AdapterUpdate.Single(wallpaper.wallpaper.id.toString(), newState)
        )
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

    enum class WallpaperState {
        UNSELECTED, SELECTED, APPROVED, DELETED
    }

    sealed class AdapterUpdate {
        class Single(val id: String, val state: WallpaperState) : AdapterUpdate()
        object All : AdapterUpdate()
    }
}