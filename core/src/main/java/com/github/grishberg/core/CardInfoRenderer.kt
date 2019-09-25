package com.github.grishberg.core

interface CardInfoRenderer {
    fun showUserName(userName: String, showAsLink: Boolean)
    fun showFullUserName(fullName: String)
}