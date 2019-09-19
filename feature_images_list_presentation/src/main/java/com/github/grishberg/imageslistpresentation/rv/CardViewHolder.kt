package com.github.grishberg.imageslistpresentation.rv

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.grishberg.imageslistpresentation.R

class CardViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
    val image = rootView.findViewById<ImageView>(R.id.cardImage)

    fun showDefaultBackgroun() {
        image.setImageResource(R.drawable.ic_image_placeholder)
    }

    fun showTargetBitmap(bitmap: Bitmap) {
        image.setImageBitmap(bitmap)
    }
}