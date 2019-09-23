package com.github.grishberg.imageslist

import com.github.grishberg.core.AnyCard

interface CardsListOutput {
    fun updateCards(cards: List<AnyCard>)
    fun updateCardByPosition(position: Int)
    fun showError(message: String)
}