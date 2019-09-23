package com.github.grishberg.imageslistpresentation

import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsList

class VerticalCardFactory(
    private val cardsList: CardsList
) : CardFactory {

    override fun createCard(
        id: String,
        url: String,
        imageUrl: String,
        userName: String
    ): Card {
        return VerticalListCard(id, url, imageUrl, userName, cardsList)
    }
}