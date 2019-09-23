package com.github.grishberg.imageslist

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard

typealias CardSelectedAction = (AnyCard) -> Unit

interface CardsList {
    fun requestCardsFirstPage()
    fun onCardSelected(selectedCard: AnyCard)
    fun requestImageByCard(shownCard: AnyCard): Bitmap?
    /**
     * Is called when card list scrolled.
     */
    fun onScrollStateChanged(lastVisibleItemPosition: Int)

    fun registerOutput(output: CardsListOutput)
    fun unregisterOutput(output: CardsListOutput)

    fun registerCardSelectedAction(action: CardSelectedAction)
    fun unregisterCardSelectedAction(action: CardSelectedAction)
}