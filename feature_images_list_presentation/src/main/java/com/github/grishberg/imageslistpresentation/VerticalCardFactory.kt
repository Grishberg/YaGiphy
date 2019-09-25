package com.github.grishberg.imageslistpresentation

import com.github.grishberg.contentdetails.CardInfoFactory
import com.github.grishberg.core.Card
import com.github.grishberg.core.ImagesProvider
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsList

class VerticalCardFactory(
    private val imagesProvider: ImagesProvider,
    private val cardsList: CardsList,
    private val cardInfoFactory: CardInfoFactory
) : CardFactory {

    override fun createCard(
        id: String,
        imageUrl: String,
        userName: String,
        userDisplayName: String?,
        profileUrl: String?
    ): Card {
        return VerticalListCard(
            id, imageUrl, userName, userDisplayName, profileUrl,
            imagesProvider, cardsList, cardInfoFactory
        )
    }
}