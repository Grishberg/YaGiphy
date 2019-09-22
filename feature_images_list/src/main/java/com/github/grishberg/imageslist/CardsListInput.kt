package com.github.grishberg.imageslist

import com.github.grishberg.core.AnyCard

interface CardsListInput {
    interface CardsReceivedAction {
        fun onCardsReceived(cardsPage: List<AnyCard>)
        fun onError(message: String)
    }

    fun requestTopCards(offset: Int)
    fun registerCardsReceivedAction(action: CardsReceivedAction)
    fun unRegisterCardsReceivedAction(action: CardsReceivedAction)
}