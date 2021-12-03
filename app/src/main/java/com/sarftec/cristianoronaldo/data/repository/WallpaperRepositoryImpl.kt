package com.sarftec.cristianoronaldo.data.repository

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.data.firebase.mapper.FirebaseCategoryMapper
import com.sarftec.cristianoronaldo.data.firebase.mapper.FirebaseWallpaperMapper
import com.sarftec.cristianoronaldo.data.firebase.paging.FirebasePagingInteractor
import com.sarftec.cristianoronaldo.data.firebase.repository.FirebaseCategoryRepository
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebasePopularWallpaperRepository
import com.sarftec.cristianoronaldo.data.firebase.repository.walllpaper.FirebaseRecentWallpaperRepository
import com.sarftec.cristianoronaldo.domain.model.CR7Category
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.domain.repository.WallpaperRepository
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val pagingInteractor: FirebasePagingInteractor,
    private val recentWallpaperRepository: FirebaseRecentWallpaperRepository,
    private val popularWallpaperRepository: FirebasePopularWallpaperRepository,
    private val categoryRepository: FirebaseCategoryRepository,
    private val categoryMapper: FirebaseCategoryMapper,
    private val wallpaperMapper: FirebaseWallpaperMapper
) : WallpaperRepository {

    override suspend fun getWallpapersForSelection(section: WallpaperRepository.RepoSection): Resource<Flow<PagingData<CR7Wallpaper>>> {
        return withContext(Dispatchers.IO) {
            val firebaseBaseRepository = when (section) {
                is WallpaperRepository.RepoSection.Recent -> recentWallpaperRepository
                is WallpaperRepository.RepoSection.Popular -> popularWallpaperRepository
                is WallpaperRepository.RepoSection.Favorite -> {
                    return@withContext Resource.success(
                        pagingInteractor.getFavoriteWallpaperFlow()
                    )
                }
                else -> return@withContext Resource.error("Not yet implemented => $section")
            }
            if (WallpaperRepository.isInitialSelection(section)) {
                Resource.success(
                    pagingInteractor.getInitialWallpaperFlow(firebaseBaseRepository)
                )
            } else {
                Resource.success(
                    pagingInteractor.getWallpaperFlowForId(section.id, firebaseBaseRepository)
                )
            }
        }
    }

    override suspend fun getWallpapersForCategory(categoryId: String): Resource<Flow<PagingData<CR7Wallpaper>>> {
        return Resource.success(
            pagingInteractor.getCategoryWallpaperFlow(categoryId)
        )
        //return Resource.error("Note implemented!")
    }

    override suspend fun getWallpaperCategories(): Resource<List<CR7Category>> {
        return withContext(Dispatchers.IO) {
            val result = categoryRepository.getCategories()
            if (result.isError()) Resource.error("${result.message}")
            else result.data!!
                .map { categoryMapper.toCR7Category(it) }
                .let { Resource.success(it) }
        }
    }

    override suspend fun createWallpaper(
        section: WallpaperRepository.RepoSection,
        imageInfo: ImageInfo,
        categoryId: String
    ): Resource<CR7Wallpaper> {
        return withContext(Dispatchers.IO) {
            val wallpaper = wallpaperMapper.toFirebaseWallpaper(
                CR7Wallpaper(
                    0,
                    0,
                    0,
                    imageInfo.toFullName(),
                    section.name,
                    categoryId
                )
            )
            recentWallpaperRepository.createWallpaper(wallpaper).also {
                //categoryRepository.addWallpaperToCategory(wallpaper)
            }
        }
    }

    override suspend fun deleteWallpaper(wallpaper: CR7Wallpaper): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            recentWallpaperRepository.deleteWallpaper(
                wallpaperMapper.toFirebaseWallpaper(
                    wallpaper
                )
            )
        }
    }
}