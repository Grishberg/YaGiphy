package com.github.grishberg.contentdetails

import com.github.grishberg.imageslist.Card

interface ContentDetails {
    fun onCardSelected(card: Card)

    /**
     * Is called when user clicked by user name.
     */
    fun onUserNameClicked()

    fun requestCardById(cardId: String)

    fun registerOutput(output: ContentDetailsOutput)

    fun registerUserProfileOutput(output: UserProfileOutput)

}