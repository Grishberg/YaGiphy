package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetailsOutput {
    /**
     * Is called when content details is ready.
     */
    fun showTwitterHashTag(tag: TwitterHashTag)

    /**
     * Is call when user clicked to card.
     */
    fun showCardDetails(card: Card)

    /**
     * Is called when image for card received.
     */
    fun updateCardImage()

    /**
     * Is called when error happens.
     */
    fun showError(message: String)
}