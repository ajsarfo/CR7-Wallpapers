package com.sarftec.cristianoronaldo.view.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarftec.cristianoronaldo.domain.model.CR7Category
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.domain.usecase.category.GetCategories
import com.sarftec.cristianoronaldo.domain.usecase.image.GetImage
import com.sarftec.cristianoronaldo.utils.Resource
import com.sarftec.cristianoronaldo.view.mapper.CategoryUIMapper
import com.sarftec.cristianoronaldo.view.model.CategoryUI
import com.sarftec.cristianoronaldo.view.model.WallpaperUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
    getImage: GetImage,
    private val categoryUIMapper: CategoryUIMapper,
    private val imageRepository: ImageRepository
) : BaseViewModel<WallpaperUI.Wallpaper>(getImage) {

    private val _categories = MutableLiveData<Resource<List<CategoryUI>>>()
    val categories: LiveData<Resource<List<CategoryUI>>>
        get() = _categories

    fun loadCategories() {
        _categories.value = Resource.loading()
        viewModelScope.launch {
            getCategories.execute(GetCategories.EmptyParam).categories.let { resource ->
                if (resource.isSuccess()) populateLiveData(resource.data)
                else _categories.value = Resource.error(
                    "Error occured => ${resource.message}"
                )
            }
        }
    }

    suspend fun getImage(categoryUI: CategoryUI.Category) : Resource<Bitmap> {
        return imageRepository.getImage(categoryUI.category.imageLocation)
    }

    private fun populateLiveData(result: List<CR7Category>?) {
        _categories.value = Resource.success(
            result?.map { categoryUIMapper.toCategoryUI(it) }
        )
    }
}