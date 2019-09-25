package com.github.grishberg.imageslistpresentation

import com.github.grishberg.core.Card
import com.github.grishberg.core.ImagesProvider
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsList

class VerticalCardFactory(
    private val imagesProvider: ImagesProvider,
    private val cardsList: CardsList
) : CardFactory {

    override fun createCard(
        id: String,
        imageUrl: String,
        userName: String
    ): Card {
        return VerticalListCard(id, imageUrl, userName, imagesProvider, cardsList)
    }
}