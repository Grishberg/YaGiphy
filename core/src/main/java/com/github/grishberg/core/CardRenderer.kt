package com.github.grishberg.core

import android.graphics.Bitmap

interface CardRenderer {
    fun showDefaultBackground()
    fun showTargetBitmap(bitmap: Bitmap)
    fun animate()
}