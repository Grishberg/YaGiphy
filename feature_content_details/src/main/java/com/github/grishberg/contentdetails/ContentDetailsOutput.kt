package com.github.grishberg.contentdetails

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