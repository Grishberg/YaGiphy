package com.github.grishberg.imageslist

import com.github.grishberg.core.AnyCard

interface CardsListOutput {
    fun updateCards(cards: List<AnyCard>)
    fun updateCardImage(card: AnyCard)
}