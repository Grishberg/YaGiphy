package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card
import com.github.grishberg.core.ImageHolder

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
    fun updateCardImage(image: ImageHolder)

    /**
     * Is called when error happens.
     */
    fun showError(message: String)

    /**
     * Is called when need to show stub image.
     */
    fun showStubImage()
}