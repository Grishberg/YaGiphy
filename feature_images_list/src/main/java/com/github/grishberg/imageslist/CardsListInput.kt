package com.github.grishberg.imageslist

import com.github.grishberg.core.Card

interface CardsListInput {
    interface CardsReceivedAction {
        fun onCardsReceived(cards: List<Card<*>>)
        fun onError(message: String)
    }

    fun requestTopCards(offset: Int)
    fun registerCardsReceivedAction(action: CardsReceivedAction)
    fun unRegisterCardsReceivedAction(action: CardsReceivedAction)
}