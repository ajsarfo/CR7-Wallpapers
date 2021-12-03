package com.sarftec.cristianoronaldo.data.injection

import com.sarftec.cristianoronaldo.data.cache.DiskImageCacheQualifier
import com.sarftec.cristianoronaldo.data.cache.ImageCache
import com.sarftec.cristianoronaldo.data.cache.InMemoryImageCacheQualifier
import com.sarftec.cristianoronaldo.data.cache.impl.DiskImageCache
import com.sarftec.cristianoronaldo.data.cache.impl.InMemoryImageCache
import com.sarftec.cristianoronaldo.data.downloader.ImageDownloader
import com.sarftec.cristianoronaldo.data.downloader.ImageDownloaderImpl
import com.sarftec.cristianoronaldo.data.repository.*
import com.sarftec.cristianoronaldo.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InterfaceInjection {

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

    @InMemoryImageCacheQualifier
    @Binds
    fun diskImageCache(cache: DiskImageCache) : ImageCache

    @DiskImageCacheQualifier
    @Binds
    fun inMemoryImageCache(cache: InMemoryImageCache) : ImageCache

    @Binds
    fun imageDownloader(downloader: ImageDownloaderImpl) : ImageDownloader
}