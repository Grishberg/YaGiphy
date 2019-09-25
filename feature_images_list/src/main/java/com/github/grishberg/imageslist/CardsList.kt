package com.github.grishberg.imageslist

import com.github.grishberg.core.Card
import com.github.grishberg.core.ImageHolder

typealias CardSelectedAction = (Card) -> Unit

interface CardsList {
    fun requestCardsFirstPage()
    fun onCardSelected(selectedCard: Card)
    fun requestImageByCard(shownCard: Card): ImageHolder
    /**
     * Is called when card list scrolled.
     */
    fun onScrollStateChanged(lastVisibleItemPosition: Int)

    fun registerOutput(output: CardsListOutput)

    fun registerCardSelectedAction(action: CardSelectedAction)
}