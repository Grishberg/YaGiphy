package com.github.grishberg.yagiphy.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.github.grishberg.contentdetails.UserProfileOutput
import com.github.grishberg.imageslist.Card

/**
 * Opens user profile screen
 */
class UserProfileScreenDelegate(
    private val appContext: Context
) : UserProfileOutput {
    override fun showUserProfileScreen(selectedCard: Card) {
        selectedCard.openUserProfile { url ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }
    }
}