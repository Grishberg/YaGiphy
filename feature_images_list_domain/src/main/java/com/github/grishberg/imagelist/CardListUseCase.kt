package com.github.grishberg.imagelist

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.imageslist.CardSelectedAction
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListInput
import com.github.grishberg.imageslist.CardsListOutput
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardListUseCase(
    private val uiScope: CoroutineScope,
    private val input: CardsListInput,
    private val imagesInput: CardImageGateway
) : CardsList, CardImageGateway.ImageReadyAction {
    private val outputs = mutableListOf<CardsListOutput>()
    private val cardSelectedActions = mutableListOf<CardSelectedAction>()
    private val cardsList = mutableListOf<Card>()
    private var isLoading: Boolean = false

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        uiScope.launch(Dispatchers.Main) {
            outputs.forEach { it.showError(exception.message.orEmpty()) }
        }
    }

    init {
        imagesInput.registerImageReadyAction(this)
    }

    override fun onCardSelected(selectedCard: Card) {
        cardSelectedActions.forEach { it.invoke(selectedCard) }
    }

    override fun requestImageByCard(shownCard: Card): Bitmap? =
        imagesInput.requestImageForCard(shownCard)

    override fun requestCardsFirstPage() {
        uiScope.launch(errorHandler) {
            val cards = input.requestTopCards(0)
            cardsList.clear()
            onCardsReceived(cards)
        }
    }

    override fun onScrollStateChanged(lastVisibleItemPosition: Int) {
        if (isLoading || lastVisibleItemPosition < cardsList.size - 1) {
            return
        }

        isLoading = true
        uiScope.launch(errorHandler) {
            val cards = input.requestTopCards(cardsList.size)
            onCardsReceived(cards)
        }
    }

    private fun onCardsReceived(cardsPage: List<Card>) {
        isLoading = false
        cardsPage.forEach { imagesInput.requestImageForCard(it) }
        cardsList.addAll(cardsPage)
        outputs.forEach { it.updateCards(cardsList) }
    }

    override fun onImageReadyForCard(targetCard: Card) {
        val targetItemPos = findCardPosition(targetCard)
        if (targetItemPos < 0) {
            return
        }
        outputs.forEach { it.updateCardByPosition(targetItemPos) }
    }

    private fun findCardPosition(targetCard: Card): Int = cardsList.lastIndexOf(targetCard)

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