package com.github.grishberg.main.domain

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList

/**
 * Application use case, contains main logic for switching between screens.
 */
class ApplicationUseCase(
    cardsList: CardsList,
    private val contentDetails: ContentDetails
) {
    private val cardListState = ListState()
    private val detailedState = DetailedState()
    private var state: State = cardListState
    private val outputBounds = mutableListOf<OutputBounds>()

    init {
        cardsList.registerCardSelectedAction {
            state.onCardSelected(it)
        }
        cardsList.requestCardsFirstPage()
    }

    /**
     * Is called when user press back.
     */
    fun onBackPressed(): Boolean = state.onBackPressed()

    fun registerOutputBounds(output: OutputBounds) {
        outputBounds.add(output)
    }

    fun onRequestedByDeepLink(id: String) {
        state.onRequestedByDeepLink(id)
    }

    private inner class DetailedState : State {
        override fun onBackPressed(): Boolean {
            state = cardListState
            notifyShowCardList()
            return true
        }

        private fun notifyShowCardList() {
            outputBounds.forEach { it.showCardsList() }
        }

        override fun onRequestedByDeepLink(cardId: String) {
            contentDetails.requestCardById(cardId)
        }
    }

    private inner class ListState : State {
        override fun onCardSelected(selectedCard: Card) {
            state = detailedState
            contentDetails.onCardSelected(selectedCard)
            notifyShowDetailedInformation()
        }

        override fun onRequestedByDeepLink(cardId: String) {
            state = detailedState
            contentDetails.requestCardById(cardId)
            notifyShowDetailedInformation()
        }

        private fun notifyShowDetailedInformation() {
            outputBounds.forEach { it.showDetailedInformation() }
        }

    }

    private interface State {
        fun onBackPressed() = false
        fun onCardSelected(selectedCard: Card) = Unit
        fun onRequestedByDeepLink(cardId: String)
    }
}