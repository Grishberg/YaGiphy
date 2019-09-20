package com.github.grishberg.core

import android.graphics.Bitmap

interface Card<R : CardRenderer> {
    fun requestImage(delegate: GetImageDelegate)
    fun render(renderer: R)
    fun isContentTheSame(card: Card<*>): Boolean
    fun updateBitmap(bitmap: Bitmap)
}