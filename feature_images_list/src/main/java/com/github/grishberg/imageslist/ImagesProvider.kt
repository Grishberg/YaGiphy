package com.github.grishberg.imageslist

/**
 * Provides images.
 */
interface ImagesProvider {
    fun getImageOrEmptyHolder(card: Card): ImageHolder
}