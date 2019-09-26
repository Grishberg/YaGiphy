package com.github.grishberg.contentdetails

import com.github.grishberg.imageslist.CardInfo

interface CardInfoFactory {
    fun create(
        userName: String,
        displayName: String?
    ): CardInfo
}