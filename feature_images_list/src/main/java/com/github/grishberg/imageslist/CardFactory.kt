package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

interface CardFactory {
    fun createCard(
        id: String,
        imageUrl: String,
        userName: String
    ): Card

    object STUB : CardFactory {
        override fun createCard(
            id: String,
            imageUrl: String,
            userName: String
        ): Card {
            throw IllegalStateException("This is Stub, use real implementation instead.")
        }
    }
}