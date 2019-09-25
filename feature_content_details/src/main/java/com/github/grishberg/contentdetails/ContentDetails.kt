package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetails {
    fun onCardSelected(card: Card)
    /**
     * Is called when user clicked by twitter account button
     */
    fun onTwitterHashTagClicked()

    fun requestCardById(cardId: String)

    fun registerOutput(output: ContentDetailsOutput)
    fun unregisterOutput(output: ContentDetailsOutput)

    fun registerTwitterOutput(output: TwitterOutputBounds)
}