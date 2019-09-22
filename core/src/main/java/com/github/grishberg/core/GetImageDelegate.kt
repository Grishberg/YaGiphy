package com.github.grishberg.core

import android.graphics.Bitmap

interface GetImageDelegate {
    fun getImageByUrl(card: AnyCard, url: String): Bitmap?
}