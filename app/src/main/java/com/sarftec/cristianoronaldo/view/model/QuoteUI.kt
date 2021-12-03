package com.sarftec.cristianoronaldo.view.model

import com.sarftec.cristianoronaldo.domain.model.CR7Quote

sealed class QuoteUI {
    class Quote(val quote: CR7Quote) : QuoteUI()
}