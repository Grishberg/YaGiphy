package com.github.grishberg.imagelist

import android.graphics.Bitmap
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListInput
import com.github.grishberg.imageslist.CardsListOutput

class ImageListUseCase(
    private val input: CardsListInput,
    private val imagesInput: CardImageGateway
) : CardsList, CardsListInput.CardsReceivedAction, CardImageGateway.ImageReadyAction {
    private val outputs = mutableListOf<CardsListOutput>()
    private var pageOffset = 0
    private val cardsList = mutableListOf<AnyCard>()
    private var isLoading: Boolean = false

    init {
        input.registerCardsReceivedAction(this)
        imagesInput.registerImageReadyAction(this)
    }

    override fun onCardSelected(selectedCardUrl: String) {

    }

    override fun requestImageByCard(shownCard: AnyCard): Bitmap? =
        imagesInput.requestImageForCard(shownCard)

    override fun requestCardsFirstPage() {
        cardsList.clear()
        input.requestTopCards(0)
        pageOffset++
    }

    override fun onCardsReceived(cardsPage: List<AnyCard>) {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImageReadyForCard(targetCard: AnyCard) {
        val targetItemPos = findCardPosition(targetCard)
        if (targetItemPos < 0) {
            return
        }
        for (output in outputs) {
            output.updateCardByPosition(targetItemPos)
        }
    }

    private fun findCardPosition(targetCard: AnyCard): Int = cardsList.lastIndexOf(targetCard)

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
}