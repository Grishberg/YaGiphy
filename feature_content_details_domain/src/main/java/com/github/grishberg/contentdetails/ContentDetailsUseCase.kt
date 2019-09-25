package com.github.grishberg.contentdetails

import com.github.grishberg.core.*
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
    private val userProfileOutputs = mutableListOf<UserProfileOutput>()
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
        showCardDetails(card.provideCardInfo())
        requestImageForCard(card)
        requestTwitterHashTag(card)
    }

    private fun requestTwitterHashTag(card: Card) {
        uiScope.launch(errorHandler) {
            currentCardTwitterHashTag = input.requestTwitterUserName(card)
            outputs.forEach { it.showTwitterHashTag(currentCardTwitterHashTag) }
        }
    }

    private fun showCardDetails(cardInfo: CardInfo) {
        outputs.forEach { it.showCardDetails(cardInfo) }
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
            outputs.forEach { it.showCardDetails(card.provideCardInfo()) }
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

    override fun onUserNameClicked() {
        currentCard?.let { card ->
            userProfileOutputs.forEach { it.showUserProfileScreen(card) }
        }
    }

    override fun registerOutput(output: ContentDetailsOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: ContentDetailsOutput) {
        outputs.remove(output)
    }

    override fun registerUserProfileOutput(output: UserProfileOutput) {
        userProfileOutputs.add(output)
    }
}