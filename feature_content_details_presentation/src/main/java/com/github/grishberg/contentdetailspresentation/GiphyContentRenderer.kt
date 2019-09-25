package com.github.grishberg.contentdetailspresentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.core.ImageHolder

class GiphyContentRenderer(
    private val imageView: ImageView,
    private val twitterHashTitle: TextView
) {
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

    fun hideTwitterHashTag() {
        twitterHashTitle.visibility = View.GONE
    }
}