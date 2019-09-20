package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetailsOutput {
    /**
     * Is called when content details is ready.
     */
    fun showContentDetails(content: Content)

    /**
     * Is called when image for card received.
     */
    fun updateCardImage(card: Card<*>)

    /**
     * Is called when error happens.
     */
    fun showError(message: String)
}