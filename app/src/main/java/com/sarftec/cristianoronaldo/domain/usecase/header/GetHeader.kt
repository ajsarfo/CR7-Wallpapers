package com.sarftec.cristianoronaldo.domain.usecase.header

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class GetHeader @Inject constructor(
    private val imageRepository: ImageRepository
) : UseCase<GetHeader.HeaderParam, GetHeader.HeaderResult>() {

    override suspend fun execute(param: HeaderParam?): HeaderResult {
      if(param == null) return HeaderResult(Resource.error("Header param NULL!"))
        return HeaderResult(imageRepository.getImage(HEADER_IMAGE_LOCATION))
    }

    object HeaderParam : Param
    class HeaderResult(val result: Resource<Bitmap>) : Response

    companion object {
        private const val HEADER_IMAGE_LOCATION = "header/header.jpg"
    }
}