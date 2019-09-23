package com.github.grishberg.contentdetails

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.core.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ContentDetailsUseCase(
    private val uiScope: CoroutineScope,
    private val imageInput: CardImageGateway,
    private val input: ContentDetailsInput
) : ContentDetails, CardImageGateway.ImageReadyAction {
    private var currentCard: AnyCard? = null
    private val outputs = mutableListOf<ContentDetailsOutput>()

    init {
        imageInput.registerImageReadyAction(this)
    }

    override fun onContentDetailsCardSelected(card: AnyCard) {
        currentCard = card
        uiScope.launch {
            val content = input.requestContentDetails(card)
            notifyContentReceived(content)
        }
    }

    private fun notifyContentReceived(content: Content) {
        for(output in outputs) {
            output.showContentDetails(content)
        }
    }

    override fun getImageForUrl(selectedCard: AnyCard): Bitmap? =
        imageInput.requestImageForCard(selectedCard)

    override fun onImageReadyForCard(targetCard: AnyCard) {
        if (targetCard != currentCard) {
            return
        }
        for(output in outputs) {
            output.updateCardImage()
        }
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