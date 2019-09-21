package com.github.grishberg.imageslistpresentation

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Card
import com.github.grishberg.core.GetImageDelegate
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardViewHolder

internal class VerticalListCard(
    private val id: String,
    private val url: String,
    private val cardList: CardsList
) : Card<CardViewHolder> {

    override fun requestImage(delegate: GetImageDelegate) {
        delegate.getImageByUrl(this, id, url)
    }

    override fun render(renderer: CardViewHolder) {
        val bitmap: Bitmap? = cardList.requestImageForCard(this)
        if (bitmap == null) {
            renderer.showDefaultBackground()
            return
        }
        renderer.showTargetBitmap(bitmap)
    }

    override fun isContentTheSame(card: AnyCard): Boolean {
        if (card !is VerticalListCard) {
            return false
        }
        return id == card.id
    }
}