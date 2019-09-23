package com.github.grishberg.imageslistpresentation

import com.github.grishberg.contentdetails.ContentDetailsFactory
import com.github.grishberg.core.AnyCard
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsList

class VerticalCardFactory(
    private val cardsList: CardsList
) : CardFactory {
    lateinit var contentDetailsFactory: ContentDetailsFactory

    override fun createCard(
        id: String,
        url: String,
        imageUrl: String,
        userName: String
    ): AnyCard {
        return VerticalListCard(id, url, imageUrl, userName, cardsList, contentDetailsFactory)
    }
}