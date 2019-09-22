package com.github.grishberg.core

import android.graphics.Bitmap

typealias AnyCard = Card<*>

interface Card<R : CardRenderer> {
    fun requestImage(delegate: GetImageDelegate): Bitmap?
    fun render(renderer: R)
    fun isContentTheSame(card: AnyCard): Boolean
    fun handleClick()
}