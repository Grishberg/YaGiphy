package com.github.grishberg.contentdetailspresentation

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import com.github.grishberg.contentderailspresentation.R
import com.github.grishberg.core.ContentRenderer

class GiphyContentRenderer(
    private val imageView: ImageView,
    private val twitterHashTitle: TextView
) : ContentRenderer {
    override fun showImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun showTwitterHashTitle(twitterName: String) {
        twitterHashTitle.text = twitterName
    }

    override fun showImageStub() {
        imageView.setImageResource(R.drawable.ic_image_placeholder)
    }
}