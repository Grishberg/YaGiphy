package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway

class ContentDetailsUseCase(
    private val imageGateway: CardImageGateway,
    private val input: ContentDetailsInput,
    private val output: ContentDetailsOutput
) : ContentDetails, ContentDetailsInput.ContentDetailsReceivedAction,
    CardImageGateway.ImageReadyAction {

    init {
        input.registerContentReceivedAction(this)
        imageGateway.registerImageReadyAction(this)
    }

    override fun onContentDetailsCardSelected(card: Card) {
        input.requestContentDetails(card)
    }

    override fun onContendReceived(content: Content) {
        output.showContentDetails(content)
    }

    override fun onImageReadyForCard(targetCard: Card) {
        output.updateCardImage(targetCard)
    }

    override fun onContentReceiveError(message: String) {
        output.showError(message)
    }

    override fun onBackPressed() {

    }
}