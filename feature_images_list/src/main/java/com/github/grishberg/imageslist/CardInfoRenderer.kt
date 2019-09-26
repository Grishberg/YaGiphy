package com.github.grishberg.imageslist

interface CardInfoRenderer {
    fun showUserName(userName: String, showAsLink: Boolean)
    fun showFullUserName(fullName: String)
}