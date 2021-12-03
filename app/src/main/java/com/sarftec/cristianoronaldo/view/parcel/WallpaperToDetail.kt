package com.sarftec.cristianoronaldo.view.parcel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class WallpaperToDetail(
    val wallpaperId: Long,
    val selection: Int
) : Parcelable {

    companion object {
        const val RECENT = 0
        const val POPULAR = 1
        const val FAVORITE = 3
    }
}