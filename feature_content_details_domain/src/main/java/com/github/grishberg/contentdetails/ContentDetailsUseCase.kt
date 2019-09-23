package com.github.grishberg.contentdetails

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ContentDetailsUseCase(
    private val uiScope: CoroutineScope,
    private val imageInput: CardImageGateway,
    private val input: ContentDetailsInput
) : ContentDetails, CardImageGateway.ImageReadyAction {
    private var currentCard: Card? = null
    private val outputs = mutableListOf<ContentDetailsOutput>()

    init {
        imageInput.registerImageReadyAction(this)
    }

    override fun onCardSelected(card: Card) {
        currentCard = card
        notifyCardSelected(card)
        uiScope.launch {
            val twitterHashTag = input.requestTwitterUserName(card)
            notifyTwitterHashTagReceived(twitterHashTag)
        }
    }

    private fun notifyCardSelected(card: Card) {
        for (output in outputs) {
            output.showCardDetails(card)
        }
    }

    private fun notifyTwitterHashTagReceived(twitterHashTag: TwitterHashTag) {
        for(output in outputs) {
            output.showTwitterHashTag(twitterHashTag)
        }
    }

    override fun getImageForUrl(selectedCard: Card): Bitmap? =
        imageInput.requestImageForCard(selectedCard)

    override fun onImageReadyForCard(targetCard: Card) {
        if (targetCard != currentCard) {
            return
        }
        for(output in outputs) {
            output.updateCardImage()
        }
    }

    override fun onTwitterHashTagClicked() {
        //TODO: open twitter account
    }

    /**
     * TODO: handle errors.
     */
    private fun onContentReceiveError(message: String) {
        for(output in outputs) {
            output.showError(message)
        }
    }

    override fun registerOutput(output: ContentDetailsOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: ContentDetailsOutput) {
        outputs.remove(output)
    }
}