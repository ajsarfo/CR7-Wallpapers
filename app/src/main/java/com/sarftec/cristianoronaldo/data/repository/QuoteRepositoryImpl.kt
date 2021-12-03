package com.sarftec.cristianoronaldo.data.repository

import com.sarftec.cristianoronaldo.data.firebase.mapper.FirebaseQuoteMapper
import com.sarftec.cristianoronaldo.data.firebase.repository.FirebaseQuoteRepository
import com.sarftec.cristianoronaldo.domain.model.CR7Quote
import com.sarftec.cristianoronaldo.domain.repository.QuoteRepository
import com.sarftec.cristianoronaldo.utils.Resource
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val mapper: FirebaseQuoteMapper,
    private val firebaseQuoteRepository: FirebaseQuoteRepository
) : QuoteRepository {

    override suspend fun getCR7Quotes(): Resource<List<CR7Quote>> {
        return try {
            firebaseQuoteRepository.getQuotes().let { result ->
                if(result.isSuccess()) Resource.success(
                    result.data!!.map { mapper.toCR7Quote(it) }
                )
                else Resource.error("${result.message}")
            }
        } catch (e: Exception) {
            Resource.error(e.message)
        }
    }
}