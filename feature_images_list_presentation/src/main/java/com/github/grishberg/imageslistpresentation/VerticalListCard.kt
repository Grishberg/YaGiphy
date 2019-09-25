package com.github.grishberg.imageslistpresentation

import com.github.grishberg.contentdetails.CardInfoFactory
import com.github.grishberg.core.*
import com.github.grishberg.imageslist.CardsList

/**
 * Abstraction of list item (not DTO).
 * Encapsulate interacting with {@link Card}.
 */
internal class VerticalListCard(
    private val id: String,
    override val imageUrl: String,
    private val userName: String,
    private val displayName: String?,
    private val profileUrl: String?,
    private val imagesProvider: ImagesProvider,
    private val cardsList: CardsList,
    private val cardInfoFactory: CardInfoFactory
) : Card {
    private var hasImage = false
    private val cardInfo: CardInfo by lazy { cardInfoFactory.create(userName, displayName) }

    override val twitterUserName: String
        get() = userName

    override fun render(renderer: CardRenderer) {
        val imageHolder: ImageHolder = imagesProvider.getImageOrEmptyHolder(this)
        if (imageHolder == ImageHolder.EMPTY) {
            renderer.showDefaultBackground()
            return
        }
        val shouldAnimate = !hasImage
        hasImage = true
        renderer.showTargetBitmap(imageHolder)

        if (shouldAnimate) {
            renderer.animate()
        }
    }

    override fun handleClick() {
        cardsList.onCardSelected(this)
    }

    override fun provideCardInfo(): CardInfo {
        return cardInfo
    }

    override fun openUserProfile(openUrlDelegate: (String) -> Unit) {
        profileUrl?.let { openUrlDelegate(it) }
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