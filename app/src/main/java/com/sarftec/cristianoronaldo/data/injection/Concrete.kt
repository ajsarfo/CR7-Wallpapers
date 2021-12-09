package com.sarftec.cristianoronaldo.data.injection

import android.content.Context
import com.sarftec.cristianoronaldo.data.room.CR7Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Concrete {

    @Singleton
    @Provides
    fun appDatabase(@ApplicationContext context: Context) : CR7Database {
        return CR7Database.getInstance(context)
    }
}