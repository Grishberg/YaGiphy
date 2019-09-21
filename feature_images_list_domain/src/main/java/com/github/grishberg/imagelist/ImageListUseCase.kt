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

    init {
        input.registerCardsReceivedAction(this)
        imagesInput.registerImageReadyAction(this)
    }

    override fun onCardSelected(selectedCard: AnyCard) {
        //TODO: show card.
    }

    override fun requestImageForCard(shownCard: AnyCard): Bitmap =
        imagesInput.requestImageForCard(shownCard)

    override fun requestCardsFirstPage() {
        input.requestTopCards(0)
        pageOffset++
    }

    override fun requestNextPage() {
        input.requestTopCards(pageOffset++)
    }

    override fun onCardsReceived(cardsPage: List<AnyCard>) {
        for (card in cardsPage) {
            imagesInput.requestImageForCard(card)
        }
        for (output in outputs) {
            output.updateCards(cardsList)
        }
    }

    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImageReadyForCard(targetCard: AnyCard) {
        for (output in outputs) {
            output.updateCardImage(targetCard)
        }
    }

    override fun registerOutput(output: CardsListOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: CardsListOutput) {
        outputs.remove(output)
    }
}