package com.github.grishberg.imagelist

import com.github.grishberg.imageslist.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val START_DOWNLOAD_OFFSET = 20

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

    override fun onCardSelected(selectedPosition: Int) {
        val selectedCard = cardsList[selectedPosition]
        cardSelectedActions.forEach { it.invoke(selectedCard) }
    }

    override fun requestCardsFirstPage() {
        uiScope.launch(errorHandler) {
            val cards = input.requestTopCards(0)
            cardsList.clear()
            onCardsReceived(cards)
        }
    }

    override fun onScrollStateChanged(lastVisibleItemPosition: Int) {
        if (isLoading || lastVisibleItemPosition < cardsList.size - START_DOWNLOAD_OFFSET - 1) {
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

    override fun onImageRequestError(message: String) {
        outputs.forEach { it.showError(message) }
    }

    private fun findCardPosition(targetCard: Card): Int = cardsList.lastIndexOf(targetCard)

    override fun registerOutput(output: CardsListOutput) {
        outputs.add(output)
    }

    override fun registerCardSelectedAction(action: CardSelectedAction) {
        cardSelectedActions.add(action)
    }
}