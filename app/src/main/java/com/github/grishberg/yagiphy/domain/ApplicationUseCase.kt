package com.github.grishberg.yagiphy.domain

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.imageslist.Card
import com.github.grishberg.imageslist.CardsList

/**
 * Application use case, contains main logic for switching between screens.
 */
class ApplicationUseCase(
    private val cardsList: CardsList,
    private val contentDetails: ContentDetails
) {
    private val idle = Idle()
    private val startedShowingDeepLink = StartedShowingDeepLink()
    private val cardListState = ListState()
    private val detailedState = DetailedState()
    private var state: State = idle
    private val outputBounds = mutableListOf<AppUseCaseOutput>()

    init {
        cardsList.registerCardSelectedAction {
            state.onCardSelected(it)
        }
    }

    /**
     * Entry point.
     * @param intentData has intent data as string.
     */
    fun start(intentData: String?) {
        if (intentData == null) {
            state.startWithoutDeepLink()
        } else {
            onRequestedByDeepLink(intentData)
        }
    }

    /**
     * Is called when received new intent with action VIEW.
     */
    fun handleNewIntent(intentData: String?) {
        intentData?.let { onRequestedByDeepLink(intentData) }
    }

    private fun onRequestedByDeepLink(intentData: String) {
        val cardId = intentData.substring(intentData.lastIndexOf("/") + 1)
        state.onRequestedByDeepLink(cardId)
    }

    /**
     * Is called when user press back.
     */
    fun onBackPressed(): Boolean = state.onBackPressed()

    fun registerOutput(output: AppUseCaseOutput) {
        outputBounds.add(output)
    }

    private fun showDetailedInformation() {
        outputBounds.forEach { it.showDetailedInformation() }
    }

    private fun showCardList() {
        outputBounds.forEach { it.showCardsList() }
    }

    private inner class DetailedState :
        State {
        override fun onBackPressed(): Boolean {
            state = cardListState
            showCardList()
            return true
        }

        override fun onRequestedByDeepLink(cardId: String) {
            contentDetails.requestCardById(cardId)
        }
    }

    private inner class ListState : State {
        override fun onCardSelected(selectedCard: Card) {
            state = detailedState
            contentDetails.onCardSelected(selectedCard)
            showDetailedInformation()
        }

        override fun onRequestedByDeepLink(cardId: String) {
            state = detailedState
            contentDetails.requestCardById(cardId)
            showDetailedInformation()
        }
    }

    private inner class StartedShowingDeepLink : State {
        override fun onBackPressed(): Boolean {
            state = cardListState
            cardsList.requestCardsFirstPage()
            showCardList()
            return true
        }
    }

    private inner class Idle : State {
        override fun onRequestedByDeepLink(cardId: String) {
            state = startedShowingDeepLink
            contentDetails.requestCardById(cardId)
            showDetailedInformation()
        }

        override fun startWithoutDeepLink() {
            state = cardListState
            showCardList()
            cardsList.requestCardsFirstPage()
        }
    }

    private interface State {
        fun onBackPressed() = false
        fun onCardSelected(selectedCard: Card) = Unit
        fun onRequestedByDeepLink(cardId: String) = Unit
        fun startWithoutDeepLink() = Unit
    }
}