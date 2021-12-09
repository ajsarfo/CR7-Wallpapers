package com.sarftec.cristianoronaldo.data.room.converter

import android.net.Uri
import androidx.room.TypeConverter


class UriConverter {

    @TypeConverter
    fun fromUri(uri: Uri) : String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(value: String) : Uri {
        return  Uri.parse(value)!!
    }
}