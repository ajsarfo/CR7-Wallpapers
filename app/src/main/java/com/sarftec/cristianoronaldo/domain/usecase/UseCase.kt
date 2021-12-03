package com.sarftec.cristianoronaldo.domain.usecase

abstract class UseCase<U : UseCase.Param, T: UseCase.Response> {

    abstract suspend fun execute(param: U?) : T?

    interface Param
    interface Response
}