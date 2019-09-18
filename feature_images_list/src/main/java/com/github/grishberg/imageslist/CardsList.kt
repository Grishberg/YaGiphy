package com.github.grishberg.imageslist

import com.github.grishberg.core.Card
import com.github.grishberg.core.render.Renderer

interface CardsList<R: Renderer> {
    fun onCardSelected(selectedCard: Card<R>)
}