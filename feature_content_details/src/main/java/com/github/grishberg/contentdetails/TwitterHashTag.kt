package com.github.grishberg.contentdetails

interface TwitterHashTag {
    val isValid: Boolean
    val name: String
    val accountUrl: String

    object EMPTY_HASHTAG : TwitterHashTag {
        override val isValid = false
        override val name = ""
        override val accountUrl = ""
    }
}