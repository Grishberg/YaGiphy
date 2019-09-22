package com.github.grishberg.imageslistpresentation

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Card
import com.github.grishberg.core.GetImageDelegate
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardViewHolder

/**
 * Abstraction of list item (not DTO).
 * Encapsulate interacting with {@link Card}.
 */
internal class VerticalListCard(
    private val id: String,
    private val url: String,
    private val imageUrl: String,
    private val cardsList: CardsList
) : Card<CardViewHolder> {
    private var hasImage = false

    override fun requestImage(delegate: GetImageDelegate): Bitmap? {
        return delegate.getImageByUrl(this, imageUrl)
    }

    override fun render(renderer: CardViewHolder) {
        val bitmap: Bitmap? = cardsList.requestImageByCard(this)
        if (bitmap == null) {
            renderer.showDefaultBackground()
            return
        }
        val shouldAnimate = !hasImage
        hasImage = true
        renderer.showTargetBitmap(bitmap)

        if (shouldAnimate) {
            renderer.animate()
        }
    }

    override fun handleClick() {
        cardsList.onCardSelected(url)
    }

    override fun isContentTheSame(card: AnyCard): Boolean {
        if (card !is VerticalListCard) {
            return false
        }
        return id == card.id
    }
}