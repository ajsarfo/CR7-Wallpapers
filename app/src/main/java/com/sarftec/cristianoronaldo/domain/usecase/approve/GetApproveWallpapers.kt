package com.sarftec.cristianoronaldo.domain.usecase.approve

import androidx.paging.PagingData
import com.sarftec.cristianoronaldo.domain.model.CR7Wallpaper
import com.sarftec.cristianoronaldo.domain.repository.ApproveRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetApproveWallpapers @Inject constructor(
 private val repository: ApproveRepository
) : UseCase<GetApproveWallpapers.GetApproveParam, GetApproveWallpapers.GetApproveResult>() {

    override suspend fun execute(param: GetApproveParam?): GetApproveResult {
        if(param == null) return GetApproveResult(Resource.error("Get Approve is NULL!"))
        return GetApproveResult(repository.getWallpapers())
    }

    object GetApproveParam : Param
    class GetApproveResult(val result: Resource<Flow<PagingData<CR7Wallpaper>>>) : Response
}