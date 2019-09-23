package com.github.grishberg.core

import android.graphics.Bitmap

interface ContentRenderer {
    fun showTwitterHashTitle(twitterTitle: String)
    fun showImageStub()
    fun showImage(bitmap: Bitmap)
}