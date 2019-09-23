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
    }

    /**
     * Is called when user press back.
     */
    fun onBackPressed(): Boolean =
        state.onBackPressed()


    fun registerOutputBounds(output: OutputBounds) {
        outputBounds.add(output)
    }

    private inner class DetailedState : State {
        override fun onBackPressed(): Boolean {
            state = cardListState
            notifyShowCardList()
            return true
        }

        private fun notifyShowCardList() {
            for (bounds in outputBounds) {
                bounds.showCardsList()
            }
        }
    }

    private inner class ListState : State {
        override fun onCardSelected(selectedCard: Card) {
            state = detailedState
            contentDetails.onCardSelected(selectedCard)
            notifyShowDetailedInformation()
        }

        private fun notifyShowDetailedInformation() {
            for (bounds in outputBounds) {
                bounds.showDetailedInformation()
            }
        }
    }

    private interface State {
        fun onBackPressed() = false
        fun onCardSelected(selectedCard: Card) = Unit
    }
}