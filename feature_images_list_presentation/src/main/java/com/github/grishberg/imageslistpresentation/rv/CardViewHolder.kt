package com.github.grishberg.imageslistpresentation.rv

import android.graphics.Bitmap
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.grishberg.core.CardRenderer
import com.github.grishberg.imageslistpresentation.R

private const val FADE_DURATION = 1000

internal class CardViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView), CardRenderer {
    val image = rootView.findViewById<ImageView>(R.id.cardImage)

    override fun showDefaultBackground() {
        image.visibility = View.VISIBLE
        image.setImageResource(R.drawable.ic_image_placeholder)
    }

    /**
     * Show bitmap with animation
     */
    override fun showTargetBitmap(bitmap: Bitmap) {
        image.visibility = View.VISIBLE
        image.setImageBitmap(bitmap)
    }

    override fun animate() {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        itemView.startAnimation(anim)
    }

    /**
     * Stop animation if in progress.
     */
    fun clearAnimation() {
        itemView.clearAnimation()
    }
}