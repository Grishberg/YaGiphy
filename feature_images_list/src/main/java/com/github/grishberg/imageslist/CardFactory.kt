package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

/**
 * Creates Card instances.
 */
interface CardFactory {
    fun createCard(
        id: String,
        imageUrl: String,
        userName: String,
        userDisplayName: String?,
        profileUrl: String?
    ): Card

    object STUB : CardFactory {
        override fun createCard(
            id: String,
            imageUrl: String,
            userName: String,
            userDisplayName: String?,
            profileUrl: String?
        ): Card {
            throw IllegalStateException("This is Stub, use real implementation instead.")
        }
    }
}