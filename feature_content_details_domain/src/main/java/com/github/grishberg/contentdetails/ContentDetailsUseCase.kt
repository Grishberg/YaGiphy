package com.github.grishberg.contentdetails

import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.CardImageGateway

class ContentDetailsUseCase(
    private val imageGateway: CardImageGateway,
    private val input: ContentDetailsInput
) : ContentDetails, ContentDetailsInput.ContentDetailsReceivedAction,
    CardImageGateway.ImageReadyAction {
    private var currentCard: AnyCard? = null
    private val outputs = mutableListOf<ContentDetailsOutput>()

    init {
        input.registerContentReceivedAction(this)
        imageGateway.registerImageReadyAction(this)
    }

    override fun onContentDetailsCardSelected(card: AnyCard) {
        currentCard = card
        input.requestContentDetails(card)
    }

    override fun onContendReceived(content: Content) {
        for(output in outputs) {
            output.showContentDetails(content)
        }
    }

    override fun onImageReadyForCard(targetCard: AnyCard) {
        if (targetCard != currentCard) {
            return
        }
        for(output in outputs) {
            output.updateCardImage()
        }
    }

    override fun onContentReceiveError(message: String) {
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