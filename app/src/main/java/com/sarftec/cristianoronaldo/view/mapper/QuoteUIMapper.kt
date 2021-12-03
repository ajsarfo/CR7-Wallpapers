package com.sarftec.cristianoronaldo.view.mapper

import com.sarftec.cristianoronaldo.domain.model.CR7Quote
import com.sarftec.cristianoronaldo.view.model.QuoteUI
import javax.inject.Inject

class QuoteUIMapper @Inject constructor(){

    fun toQuoteUI(quote: CR7Quote) : QuoteUI {
        return QuoteUI.Quote(quote)
    }
}