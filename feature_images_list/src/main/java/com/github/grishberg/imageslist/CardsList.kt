package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

interface CardsList {
    fun requestCardsFirstPage()
    fun requestNextPage()
    fun onCardSelected(selectedCard: Card<*>)
    fun registerOutput(output: CardsListOutput)
    fun unregisterOutput(output: CardsListOutput)
}