package com.sarftec.cristianoronaldo.domain.usecase.image

import android.graphics.Bitmap
import com.sarftec.cristianoronaldo.domain.repository.ImageRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetImage @Inject constructor(
    private val imageRepository: ImageRepository
) : UseCase<GetImage.GetImageParam, GetImage.GetImageResult>() {

    override suspend fun execute(param: GetImageParam?): GetImageResult {
        if (param == null) GetImageResult(
            Resource.error("Null get image param!")
        )
        return withContext(Dispatchers.IO) {
            GetImageResult(
                imageRepository.getImage(param!!.imageLocation)
            )
        }
    }

    class GetImageParam(val imageLocation: String) : Param
    class GetImageResult(val image: Resource<Bitmap>) : Response
}