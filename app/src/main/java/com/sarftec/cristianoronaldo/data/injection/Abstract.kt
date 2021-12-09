package com.sarftec.cristianoronaldo.data.injection

import com.sarftec.cristianoronaldo.data.cache.*
import com.sarftec.cristianoronaldo.data.repository.*
import com.sarftec.cristianoronaldo.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface Abstract {

    @Singleton
    @Binds
    fun repository(repository: WallpaperRepositoryImpl) : WallpaperRepository

    @Singleton
    @Binds
    fun imageRepository(repository: ImageRepositoryImpl) : ImageRepository

    @Singleton
    @Binds
    fun quoteRepository(repository: QuoteRepositoryImpl) : QuoteRepository

    @Singleton
    @Binds fun storageRepository(repository: StorageRepositoryImpl) : StorageRepository

    @Binds
    fun uriCache(cache: UriCacheImpl) : UriCache
}