package com.github.grishberg.imageslist

import com.github.grishberg.core.AnyCard

interface CardFactory {
    fun createCard(
        id: String,
        url: String,
        imageUrl: String,
        userName: String
    ): AnyCard

    object STUB : CardFactory {
        override fun createCard(
            id: String,
            url: String,
            imageUrl: String,
            userName: String
        ): AnyCard {
            throw IllegalStateException("This is Stub, use real implementation instead.")
        }
    }
}