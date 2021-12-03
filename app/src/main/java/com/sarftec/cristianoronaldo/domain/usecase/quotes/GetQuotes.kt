package com.sarftec.cristianoronaldo.domain.usecase.quotes

import android.util.Log
import com.sarftec.cristianoronaldo.domain.model.CR7Quote
import com.sarftec.cristianoronaldo.domain.repository.QuoteRepository
import com.sarftec.cristianoronaldo.domain.usecase.UseCase
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class GetQuotes @Inject constructor(
    private val repository: QuoteRepository
)
    : UseCase<GetQuotes.EmptyParam, GetQuotes.QuoteResult>() {

    override suspend fun execute(param: EmptyParam?): QuoteResult {
       val result = if(param == null) Resource.error("Error => GetQuotes param NULL!")
        else repository.getCR7Quotes()
        return QuoteResult(result)
    }

    object EmptyParam : Param
    class QuoteResult(val quotes: Resource<List<CR7Quote>>) : Response
}