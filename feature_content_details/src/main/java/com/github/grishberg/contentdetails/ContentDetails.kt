package com.github.grishberg.contentdetails

import android.graphics.Bitmap
import com.github.grishberg.core.Card

interface ContentDetails {
    fun onCardSelected(card: Card)
    fun registerOutput(output: ContentDetailsOutput)
    fun unregisterOutput(output: ContentDetailsOutput)
    fun getImageForUrl(selectedCard: Card): Bitmap?
    /**
     * Is called when user clicked by twitter account button
     */
    fun onTwitterHashTagClicked()
}