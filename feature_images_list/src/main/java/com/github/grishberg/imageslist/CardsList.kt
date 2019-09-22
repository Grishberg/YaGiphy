package com.github.grishberg.imageslist

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard

interface CardsList {
    fun requestCardsFirstPage()
    fun onCardSelected(selectedCardUrl: String)
    fun requestImageByCard(shownCard: AnyCard): Bitmap?
    fun registerOutput(output: CardsListOutput)
    fun unregisterOutput(output: CardsListOutput)
    /**
     * Is called when card list scrolled.
     */
    fun onScrollStateChanged(lastVisibleItemPosition: Int)
}