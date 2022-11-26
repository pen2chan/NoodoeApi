package com.penchan.noodoeapi.base

open class Event<out T>(private val content: T) {

    var isHandled = false
        private set

    fun getContentIfNotHandled() = if(isHandled) {
        null
    } else {
        isHandled = true
        content
    }

    fun peekContent() = content
}