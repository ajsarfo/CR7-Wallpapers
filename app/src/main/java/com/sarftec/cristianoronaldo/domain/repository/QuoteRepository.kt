package com.sarftec.cristianoronaldo.domain.repository

import com.sarftec.cristianoronaldo.domain.model.CR7Quote
import com.sarftec.cristianoronaldo.utils.Resource

interface QuoteRepository {
    suspend fun getCR7Quotes() : Resource<List<CR7Quote>>
}