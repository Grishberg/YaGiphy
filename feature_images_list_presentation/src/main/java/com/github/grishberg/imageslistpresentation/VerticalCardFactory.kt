package com.github.grishberg.imageslistpresentation

import com.github.grishberg.core.Card
import com.github.grishberg.core.CardFactory

class VerticalCardFactory : CardFactory {
    override fun createCard(id: String, url: String): Card<*> {
        return VerticalListCard(id, url)
    }
}