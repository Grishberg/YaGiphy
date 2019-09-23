package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

interface CardsListOutput {
    fun updateCards(cards: List<Card>)
    fun updateCardByPosition(position: Int)
    fun showError(message: String)
}