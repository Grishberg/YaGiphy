package com.github.grishberg.core

/**
 * Provides images.
 */
interface ImagesProvider {
    fun getImageOrEmptyHolder(card: Card): ImageHolder
}