package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.core.CardInfo
import com.github.grishberg.core.CardInfoRenderer

internal class CardInfoImpl(
    private val userName: String,
    private val displayUserName: String?
) : CardInfo {

    override fun render(renderer: CardInfoRenderer) {
        renderer.showUserName(userName)

        displayUserName?.let { name -> renderer.showFullUserName(name) }
    }
}