package com.github.grishberg.imagelist

import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListInput
import com.github.grishberg.imageslist.CardsListOutput

class ImageListUseCase(
    private val input: CardsListInput
) : CardsList, CardsListInput.CardsReceivedAction {
    private val outputs = mutableListOf<CardsListOutput>()

    init {
        input.registerCardsReceivedAction(this)
    }

    override fun onCardSelected(selectedCard: Card<*>) {

    }

    override fun requestCardsFirstPage() {
        input.requestTopCards(0)
    }

    override fun requestNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCardsReceived(cards: List<Card<*>>) {
        for (output in outputs) {
            output.updateCards(cards)
        }
    }

    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerOutput(output: CardsListOutput) {
        outputs.add(output)
    }

    override fun unregisterOutput(output: CardsListOutput) {
        outputs.remove(output)
    }
}