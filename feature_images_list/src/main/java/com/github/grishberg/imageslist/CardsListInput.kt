package com.github.grishberg.imageslist

interface CardsListInput {
    interface CardsReceivedAction {
        fun onCardsReceived()
    }
    fun registerCardsReceivedAction()
}