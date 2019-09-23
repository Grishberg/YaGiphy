package com.github.grishberg.imageslistpresentation

import android.graphics.Bitmap
import com.github.grishberg.contentdetails.ContentDetailsFactory
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Card
import com.github.grishberg.core.Content
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslistpresentation.rv.CardViewHolder

/**
 * Abstraction of list item (not DTO).
 * Encapsulate interacting with {@link Card}.
 */
internal class VerticalListCard(
    private val id: String,
    private val url: String,
    override val imageUrl: String,
    private val userName: String,
    private val cardsList: CardsList,
    private val contentDetailsFactory: ContentDetailsFactory
) : Card<CardViewHolder> {
    private var hasImage = false

    override val twitterUserName: String
        get() = userName

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
        cardsList.onCardSelected(this)
    }

    override fun createContent(twitterValid: Boolean): Content =
        contentDetailsFactory.createContent(
            this, url, userName,
            if (twitterValid) userName else null
        )

    override fun isContentTheSame(card: AnyCard): Boolean {
        if (card !is VerticalListCard) {
            return false
        }
        return id == card.id
    }
}