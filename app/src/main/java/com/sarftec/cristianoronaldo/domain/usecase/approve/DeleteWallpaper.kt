package com.sarftec.cristianoronaldo.domain.usecase.approve

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.ApproveRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class DeleteWallpaper @Inject constructor(
private val repository: ApproveRepository
) : UseCase<DeleteWallpaper.DeleteParam, DeleteWallpaper.DeleteResult>() {

    override suspend fun execute(param: DeleteParam?): DeleteResult {
        if (param == null) return DeleteResult(Resource.error("Error => Delete Param NULL!"))
        return DeleteResult(repository.approveWallpaper(param.wallpaper))
    }

    class DeleteParam(val wallpaper: CR7Wallpaper) : Param
    class DeleteResult(val result: Resource<Unit>) : Response
}