package com.github.grishberg.contentdetails

import com.github.grishberg.core.Content

interface ContentDetailsOutput {
    /**
     * Is called when content details is ready.
     */
    fun showContentDetails(content: Content)

    /**
     * Is called when image for card received.
     */
    fun updateCardImage()

    /**
     * Is called when error happens.
     */
    fun showError(message: String)
}