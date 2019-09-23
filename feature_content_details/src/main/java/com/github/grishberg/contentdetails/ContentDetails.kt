package com.github.grishberg.contentdetails

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard

interface ContentDetails {
    fun onContentDetailsCardSelected(card: AnyCard)
    fun registerOutput(output: ContentDetailsOutput)
    fun unregisterOutput(output: ContentDetailsOutput)
    fun getImageForUrl(selectedCard: AnyCard): Bitmap?
}