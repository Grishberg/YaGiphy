package com.github.grishberg.imageslistpresentation

import com.github.grishberg.core.AnyCard
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsList

class VerticalCardFactory(
    private val cardsList: CardsList
) : CardFactory {
    override fun createCard(id: String, url: String, imageUrl: String): AnyCard {
        return VerticalListCard(id, url, imageUrl, cardsList)
    }
}