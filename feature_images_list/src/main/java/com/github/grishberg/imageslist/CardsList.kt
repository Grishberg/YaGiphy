package com.github.grishberg.imageslist

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard

interface CardsList {
    fun requestCardsFirstPage()
    fun requestNextPage()
    fun onCardSelected(selectedCard: AnyCard)
    fun requestImageForCard(shownCard: AnyCard): Bitmap
    fun registerOutput(output: CardsListOutput)
    fun unregisterOutput(output: CardsListOutput)
}