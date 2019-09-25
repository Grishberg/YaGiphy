package com.github.grishberg.core

/**
 * Renders card item.
 */
interface CardRenderer {
    fun showDefaultBackground()
    fun showTargetBitmap(imageHolder: ImageHolder)
    fun animate()
}