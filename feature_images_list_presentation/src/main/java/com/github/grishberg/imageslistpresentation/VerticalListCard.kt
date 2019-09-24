package com.github.grishberg.imageslistpresentation

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardRenderer
import com.github.grishberg.imageslist.CardsList

/**
 * Abstraction of list item (not DTO).
 * Encapsulate interacting with {@link Card}.
 */
internal class VerticalListCard(
    private val id: String,
    private val url: String,
    override val imageUrl: String,
    private val userName: String,
    private val cardsList: CardsList
) : Card {
    private var hasImage = false

    override val twitterUserName: String
        get() = userName

    override fun render(renderer: CardRenderer) {
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
        cardsList.onCardSelected(this)
    }

    override fun isContentTheSame(card: Card): Boolean {
        if (card !is VerticalListCard) {
            return false
        }
        return id == card.id
    }

    override fun toString(): String =
        "{id=$id, userName=$userName}"
}