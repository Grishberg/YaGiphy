package com.github.grishberg.contentdetailspresentation

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.core.CardRenderer

class GiphyContentRenderer(
    private val imageView: ImageView,
    private val twitterHashTitle: TextView
) : CardRenderer {
    override fun showDefaultBackground() {
        imageView.setImageResource(R.drawable.ic_image_placeholder)
    }

    override fun showTargetBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    fun showTwitterHashTitle(twitterName: String) {
        twitterHashTitle.visibility = View.VISIBLE
        twitterHashTitle.text = twitterName
    }

    fun hideTwitterHashTag() {
        twitterHashTitle.visibility = View.GONE
    }

    override fun animate() {
        /* not used */
    }
}