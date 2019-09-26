package com.github.grishberg.yagiphy.domain

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardSelectedAction
import com.github.grishberg.imageslist.CardsList
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val TEST_ID = "1234"
private const val DEEP_LINK_URL = "http://giphy.com/gifs/$TEST_ID"

class ApplicationUseCaseTest {
    private val selectedCard = mock<Card>()
    private val useCaseOutput = mock<AppUseCaseOutput>()
    private lateinit var cardSelectedAction: CardSelectedAction
    private val cardList = mock<CardsList> {
        doAnswer { iom ->
            cardSelectedAction = iom.getArgument(0)
        }.whenever(it).registerCardSelectedAction(any())
    }
    private val contentDetails = mock<ContentDetails>()
    private val underTest = ApplicationUseCase(cardList, contentDetails)

    @Before
    fun setUp() {
        underTest.registerOutput(useCaseOutput)
    }

    @Test
    fun `request card list first page when started without deep link data`() {
        underTest.start(null)

        verify(cardList).requestCardsFirstPage()
    }

    @Test
    fun `show card list when started without deep link data`() {
        underTest.start(null)

        verify(useCaseOutput).showCardsList()
    }

    @Test
    fun `dont request first page when started by deep link`() {
        whenStartedWithDeepLink()

        verify(cardList, never()).requestCardsFirstPage()
    }

    @Test
    fun `request card by id when started by deep link`() {
        whenStartedWithDeepLink()

        verify(contentDetails).requestCardById(TEST_ID)
    }

    @Test
    fun `show content details screen when started by deep link`() {
        whenStartedWithDeepLink()

        verify(useCaseOutput).showDetailedInformation()
    }

    @Test
    fun `request card list first page after backpress when started by deep link`() {
        whenBackPressAfterStartingByDeepLink()

        verify(cardList).requestCardsFirstPage()
    }

    @Test
    fun `show card list first page after backpress when started by deep link`() {
        whenBackPressAfterStartingByDeepLink()

        verify(useCaseOutput).showCardsList()
    }

    @Test
    fun `allow exit on backpress when showing card list`() {
        underTest.start(null)

        assertFalse(underTest.onBackPressed())
    }

    @Test
    fun `dont allow exit on backpress when showing content details`() {
        whenCardSelected()

        assertTrue(underTest.onBackPressed())
    }

    @Test
    fun `show content details when showing card list and new intent with deep link`() {
        underTest.start(null)

        underTest.handleNewIntent(DEEP_LINK_URL)

        verify(useCaseOutput).showDetailedInformation()
    }

    @Test
    fun `request card by id when showing content details and new intent with deep link`() {
        whenCardSelected()

        underTest.handleNewIntent(DEEP_LINK_URL)

        verify(contentDetails).requestCardById(TEST_ID)
    }

    @Test
    fun `dont allow exit on backpress when showing deep link content details`() {
        whenCardSelected()
        underTest.handleNewIntent(DEEP_LINK_URL)

        assertTrue(underTest.onBackPressed())
    }

    @Test
    fun `allow exit on backpress when second back press after deep link content details`() {
        whenCardSelected()
        underTest.handleNewIntent(DEEP_LINK_URL)
        assertTrue(underTest.onBackPressed())

        assertFalse(underTest.onBackPressed())
    }

    private fun whenCardSelected() {
        underTest.start(null)

        cardSelectedAction(selectedCard)
    }

    private fun whenBackPressAfterStartingByDeepLink() {
        whenStartedWithDeepLink()

        underTest.onBackPressed()
    }

    private fun whenStartedWithDeepLink() {
        underTest.start(DEEP_LINK_URL)
    }
}