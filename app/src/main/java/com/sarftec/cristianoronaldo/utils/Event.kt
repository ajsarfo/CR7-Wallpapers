package com.sarftec.cristianoronaldo.utils

class Event<out T>(private val item: T) {

    var isHandled: Boolean = false

    fun getIfNotHandled() : T? {
        if(isHandled) return null
        isHandled = true
        return item
    }

    fun peek() : T = item
}