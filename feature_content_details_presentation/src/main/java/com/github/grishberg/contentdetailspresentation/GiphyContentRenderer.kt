package com.github.grishberg.contentdetailspresentation

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.imageslist.CardInfoRenderer
import com.github.grishberg.imageslist.ImageHolder

class GiphyContentRenderer(
    context: Context,
    private val imageView: ImageView,
    private val twitterHashTitle: TextView,
    private val userNameView: TextView,
    private val userFullNameView: TextView
) : CardInfoRenderer {
    private val textColor = ContextCompat.getColor(context, R.color.textColor)
    private val linkColor = ContextCompat.getColor(context, R.color.linkColor)

    fun showDefaultBackground() {
        imageView.setImageResource(R.drawable.ic_giphy_icon)
    }

    fun showTargetBitmap(imageHolder: ImageHolder) {
        imageView.setImageBitmap(imageHolder.bitmap)
    }

    fun showTwitterHashTitle(twitterName: String) {
        twitterHashTitle.visibility = View.VISIBLE
        twitterHashTitle.text = twitterName
    }

    fun hideTitles() {
        twitterHashTitle.visibility = View.GONE
        userNameView.text = ""
        userFullNameView.text = ""
    }

    override fun showUserName(userName: String, showAsLink: Boolean) {
        with(userNameView) {
            text = userName
            if (showAsLink) {
                setTextColor(linkColor)
            } else {
                setTextColor(textColor)
            }
        }
    }

    override fun showFullUserName(fullName: String) {
        userFullNameView.text = fullName
    }
}