package com.github.grishberg.imageslist

/**
 * Renders card item.
 */
interface CardRenderer {
    fun showDefaultBackground()
    fun showTargetBitmap(imageHolder: ImageHolder)
    fun animate()
}