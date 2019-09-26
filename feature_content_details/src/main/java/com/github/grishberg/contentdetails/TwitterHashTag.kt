package com.github.grishberg.contentdetails

interface TwitterHashTag {
    val isValid: Boolean
    val name: String
    val accountUrl: String

    object EMPTY : TwitterHashTag {
        override val isValid = false
        override val name = ""
        override val accountUrl = ""
    }
}