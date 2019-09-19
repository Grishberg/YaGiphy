package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

interface CardsList {
    fun onCardSelected(selectedCard: Card)
}