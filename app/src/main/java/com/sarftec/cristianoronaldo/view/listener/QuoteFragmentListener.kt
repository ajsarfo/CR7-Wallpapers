package com.sarftec.cristianoronaldo.view.listener

import com.sarftec.cristianoronaldo.view.handler.ReadWriteHandler

interface QuoteFragmentListener {
    fun getReadWriteHandler() : ReadWriteHandler
}