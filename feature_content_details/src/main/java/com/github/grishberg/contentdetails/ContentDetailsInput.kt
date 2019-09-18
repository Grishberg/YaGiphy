package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetailsInput {
    interface ContentDetailsReceivedAction {
        /**
         * Is called when content details received.
         */
        fun onContendReceived(content: Content)

        /**
         * Is called when some error is happens.
         */
        fun onContentReceiveError(message: String)
    }

    fun requestContentDetails(selectedCard: Card)
    fun registerContentReceivedAction(action: ContentDetailsReceivedAction)
    fun unregisterContentReceivedAction(action: ContentDetailsReceivedAction)
}