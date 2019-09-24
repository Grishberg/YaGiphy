package com.github.grishberg.imagelist

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.imageslist.CardSelectedAction
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListInput
import com.github.grishberg.imageslist.CardsListOutput

class ImageListUseCase(
    private val input: CardsListInput,
    private val imagesInput: CardImageGateway
) : CardsList, CardsListInput.CardsReceivedAction, CardImageGateway.ImageReadyAction {
    private val outputs = mutableListOf<CardsListOutput>()
    private val cardSelectedActions = mutableListOf<CardSelectedAction>()
    private val cardsList = mutableListOf<Card>()
    private var isLoading: Boolean = false

    init {
        input.registerCardsReceivedAction(this)
        imagesInput.registerImageReadyAction(this)
        requestCardsFirstPage()
    }

    override fun onCardSelected(selectedCard: Card) {
        for (action in cardSelectedActions) {
            action.invoke(selectedCard)
        }
    }

    override fun requestImageByCard(shownCard: Card): Bitmap? =
        imagesInput.requestImageForCard(shownCard)

    override fun requestCardsFirstPage() {
        cardsList.clear()
        input.requestTopCards(0)
    }

    override fun onCardsReceived(cardsPage: List<Card>) {
        isLoading = false
        for (card in cardsPage) {
            imagesInput.requestImageForCard(card)
        }
        cardsList.addAll(cardsPage)
        for (output in outputs) {
            output.updateCards(cardsList)
        }
    }

    override fun onError(message: String) {
        for (output in outputs) {
            output.showError(message)
        }
    }

    override fun onImageReadyForCard(targetCard: Card) {
        val targetItemPos = findCardPosition(targetCard)
        if (targetItemPos < 0) {
            return
        }
        for (output in outputs) {
            output.updateCardByPosition(targetItemPos)
        }
    }

    private fun findCardPosition(targetCard: Card): Int = cardsList.lastIndexOf(targetCard)

    override fun onScrollStateChanged(lastVisibleItemPosition: Int) {
        if (isLoading) {
            return
        }

        if (lastVisibleItemPosition == cardsList.size - 1) {
            isLoading = true
            input.requestTopCards(cardsList.size)
        }
    }

    override fun registerOutput(output: CardsListOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: CardsListOutput) {
        outputs.remove(output)
    }

    override fun registerCardSelectedAction(action: CardSelectedAction) {
        cardSelectedActions.add(action)
    }

    override fun unregisterCardSelectedAction(action: CardSelectedAction) {
        cardSelectedActions.remove(action)
    }
}