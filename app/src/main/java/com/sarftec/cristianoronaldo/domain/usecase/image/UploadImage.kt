package com.sarftec.cristianoronaldo.domain.usecase.image

import com.sarftec.cristianoronaldo.domain.model.ImageInfo
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UploadImage @Inject constructor(
    private val imageRepository: ImageRepository
): UseCase<UploadImage.UploadParam, UploadImage.UploadResult>()  {

    override suspend fun execute(param: UploadParam?): UploadResult {
        if(param == null) return UploadResult(
            Resource.error("Null upload image param!")
        )
        return withContext(Dispatchers.IO) {
            UploadResult(imageRepository.uploadWallpaper(param.file, param.imageInfo))
        }
    }

    class UploadResult(val result: Resource<Unit>) : Response
    class UploadParam(val file: File, val imageInfo: ImageInfo) : Param

}