package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.imageslist.CardInfo
import com.github.grishberg.imageslist.CardInfoRenderer

internal class CardInfoImpl(
    private val userName: String,
    private val displayUserName: String?
) : CardInfo {

    override fun render(renderer: CardInfoRenderer) {
        renderer.showUserName(userName, displayUserName != null)

        displayUserName?.let { name -> renderer.showFullUserName(name) }
    }
}