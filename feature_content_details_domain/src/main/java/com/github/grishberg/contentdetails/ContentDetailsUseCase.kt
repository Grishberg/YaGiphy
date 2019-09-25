package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.core.CoroutineDispatchers
import com.github.grishberg.core.ImageHolder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ContentDetailsUseCase(
    private val uiScope: CoroutineScope,
    private val coroutineContextProvider: CoroutineDispatchers,
    private val imageInput: CardImageGateway,
    private val input: ContentDetailsInput
) : ContentDetails, CardImageGateway.ImageReadyAction {
    private var currentCard: Card? = null
    private var currentCardTwitterHashTag: TwitterHashTag = TwitterHashTag.EMPTY_HASHTAG
    private val outputs = mutableListOf<ContentDetailsOutput>()
    private val twitterOutputs = mutableListOf<TwitterOutputBounds>()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        uiScope.launch(coroutineContextProvider.main) {
            outputs.forEach { it.showError(exception.message.orEmpty()) }
        }
    }

    init {
        imageInput.registerImageReadyAction(this)
    }

    override fun onCardSelected(card: Card) {
        currentCard = card
        currentCardTwitterHashTag = TwitterHashTag.EMPTY_HASHTAG
        showStub()
        showCardDetails(card)
        requestImageForCard(card)
        requestTwitterHashTag(card)
    }

    private fun requestTwitterHashTag(card: Card) {
        uiScope.launch(errorHandler) {
            currentCardTwitterHashTag = input.requestTwitterUserName(card)
            outputs.forEach { it.showTwitterHashTag(currentCardTwitterHashTag) }
        }
    }

    private fun showCardDetails(card: Card) {
        outputs.forEach { it.showCardDetails(card) }
    }

    override fun onImageReadyForCard(targetCard: Card) {
        if (targetCard != currentCard) {
            return
        }
        val image = imageInput.getImageOrEmptyHolder(targetCard)
        if (image != ImageHolder.EMPTY) {
            outputs.forEach { it.updateCardImage(image) }
        }
    }

    override fun onImageRequestError(message: String) {
        outputs.forEach { it.showError("Image request error: $message") }
    }

    override fun requestCardById(cardId: String) {
        showStub()
        uiScope.launch(errorHandler) {
            val card = input.requestCardById(cardId)
            currentCard = card
            outputs.forEach { it.showCardDetails(card) }
            requestImageForCard(card)
        }
    }

    private fun showStub() {
        outputs.forEach { it.showStubImage() }
    }

    private fun requestImageForCard(targetCard: Card) {
        val image = imageInput.requestImageForCard(targetCard)
        if (image != ImageHolder.EMPTY) {
            outputs.forEach { it.updateCardImage(image) }
        }
    }

    override fun onTwitterHashTagClicked() {
        twitterOutputs.forEach { it.showTwitterScreen(currentCardTwitterHashTag) }
    }

    override fun registerOutput(output: ContentDetailsOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: ContentDetailsOutput) {
        outputs.remove(output)
    }

    override fun registerTwitterOutput(output: TwitterOutputBounds) {
        twitterOutputs.add(output)
    }
}