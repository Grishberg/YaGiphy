package com.github.grishberg.imageslist

interface CardsListOutput {
    fun updateCards(cards: List<Card>)
    fun updateCardByPosition(position: Int)
    fun showError(message: String)
}