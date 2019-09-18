package com.github.grishberg.giphygateway

import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway

class GiphyRepository : CardImageGateway {
    private val actions = mutableListOf<CardImageGateway.ImageReadyAction>()

    override fun requestImageForCard(card: Card) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.add(action)
    }

    override fun unreqisterImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.remove(action)
    }
}