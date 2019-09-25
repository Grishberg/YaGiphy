package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface UserProfileOutput {
    fun showUserProfileScreen(selectedCard: Card)
}