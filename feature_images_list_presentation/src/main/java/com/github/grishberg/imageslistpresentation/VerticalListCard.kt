package com.github.grishberg.imageslistpresentation

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.GetImageDelegate
import com.github.grishberg.imageslistpresentation.rv.CardViewHolder

internal class VerticalListCard(
    private val id: String,
    private val url: String
) : Card<CardViewHolder> {

    private var bitmap: Bitmap? = null

    override fun requestImage(delegate: GetImageDelegate) {
        delegate.getImageByUrl(this, id, url)
    }

    override fun render(renderer: CardViewHolder) {
        if (bitmap == null) {
            renderer.showDefaultBackground()
            return
        }
        bitmap?.let { renderer.showTargetBitmap(it) }
    }

    override fun updateBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    override fun isContentTheSame(card: Card<*>): Boolean {
        if (card !is VerticalListCard) {
            return false
        }
        return id == card.id
    }
}