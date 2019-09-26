package com.github.grishberg.contentdetails

import com.github.grishberg.imageslist.Card

interface UserProfileOutput {
    fun showUserProfileScreen(selectedCard: Card)
}