package com.sarftec.cristianoronaldo.domain.usecase.approve

import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.ApproveRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class ApproveWallpaper @Inject constructor(
    private val repository: ApproveRepository
): UseCase<ApproveWallpaper.ApproveParam, ApproveWallpaper.ApproveResult>() {

    class ApproveParam(val wallpaper: CR7Wallpaper) : Param
    class ApproveResult(val result: Resource<Unit>) : Response

    override suspend fun execute(param: ApproveParam?): ApproveResult {
       if(param == null) return ApproveResult(Resource.error("Error => Approve Param NULL!"))
        repository.approveWallpaper(param.wallpaper)
        return ApproveResult(
            Resource.error("Not yet implemented!")
        )
    }
}