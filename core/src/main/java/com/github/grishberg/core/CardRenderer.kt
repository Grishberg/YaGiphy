package com.github.grishberg.core

import android.graphics.Bitmap

/**
 * Renders card item.
 */
interface CardRenderer {
    fun showDefaultBackground()
    fun showTargetBitmap(bitmap: Bitmap)
    fun animate()
}