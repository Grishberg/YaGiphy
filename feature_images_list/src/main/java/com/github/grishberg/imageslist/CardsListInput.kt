package com.github.grishberg.imageslist

import com.github.grishberg.core.ListItemCard

interface CardsListInput {
    interface CardsReceivedAction {
        fun onCardsReceived(cards: List<ListItemCard>)
        fun onError(message: String)
    }

    fun registerCardsReceivedAction(action: CardsReceivedAction)
    fun unRegisterCardsReceivedAction(action: CardsReceivedAction)
}