package com.sarftec.cristianoronaldo.data.firebase.mapper

import com.sarftec.cristianoronaldo.data.firebase.model.FirebaseQuote
import com.sarftec.cristianoronaldo.domain.model.CR7Quote
import javax.inject.Inject

class FirebaseQuoteMapper @Inject constructor(){

    fun toCR7Quote(quote: FirebaseQuote) : CR7Quote {
        return CR7Quote(quote.imagePath!!)
    }
}