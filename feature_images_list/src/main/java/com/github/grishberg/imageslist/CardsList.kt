package com.github.grishberg.imageslist

typealias CardSelectedAction = (Card) -> Unit

interface CardsList {
    /**
     * Is called when need to refresh top cards page.
     */
    fun requestCardsFirstPage()

    /**
     * Is called when user selected card.
     */
    fun onCardSelected(selectedPosition: Int)

    /**
     * Is called when card list scrolled.
     */
    fun onScrollStateChanged(lastVisibleItemPosition: Int)

    fun registerOutput(output: CardsListOutput)

    fun registerCardSelectedAction(action: CardSelectedAction)
}