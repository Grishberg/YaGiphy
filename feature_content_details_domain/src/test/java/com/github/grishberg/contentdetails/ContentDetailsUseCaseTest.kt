package com.github.grishberg.contentdetails

import com.github.grishberg.contentdetails.exceptions.ContentDetailsInputException
import com.github.grishberg.core.*
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class ContentDetailsUseCaseTest {
    private val scope = CoroutineTestScope()

    private val testDispatchers = TestDispatchers()
    private val twitterTag = ValidTwitterHashTag()
    private val downloadedImage = mock<ImageHolder>()
    private val cardInfo = mock<CardInfo>()
    private val card = mock<Card> {
        on { provideCardInfo() } doReturn cardInfo
    }
    private val imageInput = mock<CardImageGateway>()
    private val contentDetailsInput = mock<ContentDetailsInput> {
        onBlocking { requestTwitterUserName(any()) } doReturn twitterTag
        onBlocking { requestCardById(any()) } doReturn card
    }

    private val twitterOutput = mock<UserProfileOutput>()
    private val contentDetailsOutput = mock<ContentDetailsOutput>()

    private val underTest = createContentDetails()

    @Test
    fun `notify output card select event when card selected`() {
        underTest.onCardSelected(card)

        verify(contentDetailsOutput).showCardDetails(cardInfo)
    }

    @Test
    fun `receive twitter tag when card selected`() {
        underTest.onCardSelected(card)

        verify(contentDetailsOutput).showTwitterHashTag(twitterTag)
    }

    @Test
    fun `notify output image ready when image downloaded`() {
        underTest.onCardSelected(card)

        whenReceivedImageForCard()

        verify(contentDetailsOutput).updateCardImage(downloadedImage)
    }

    @Test
    fun `dont notify output image ready when image downloaded for old card`() {
        val oldCard = mock<Card>()
        underTest.onCardSelected(oldCard)
        underTest.onCardSelected(card)
        whenever(imageInput.requestImageForCard(oldCard)) doReturn downloadedImage

        underTest.onImageReadyForCard(oldCard)

        verify(contentDetailsOutput, never()).updateCardImage(any())
    }

    @Test
    fun `notify image loaded after receiving card by id`() {
        underTest.onCardSelected(mock())
        whenReceivedCardWithoutImage()

        whenReceivedImageForCard()

        verify(contentDetailsOutput).updateCardImage(downloadedImage)
    }

    @Test
    fun `notify error when twitter hashTag request failed`() {
        val contentDetailsInputWithError = mock<ContentDetailsInput> {
            onBlocking { requestCardById(any()) } doThrow ContentDetailsInputException("fail")
        }
        val underTest = createContentDetails(contentDetailsInputWithError)

        underTest.requestCardById("1234")

        verify(contentDetailsOutput).showError("fail")
    }

    private fun whenReceivedImageForCard() {
        whenever(imageInput.getImageOrEmptyHolder(card)) doReturn downloadedImage
        underTest.onImageReadyForCard(card)
    }

    private fun whenReceivedCardWithoutImage() {
        whenever(imageInput.requestImageForCard(card)) doReturn ImageHolder.EMPTY
        underTest.requestCardById("id")
    }

    private fun createContentDetails(contentDetails: ContentDetailsInput = contentDetailsInput) =
        ContentDetailsUseCase(scope, testDispatchers, imageInput, contentDetails).apply {
            registerOutput(contentDetailsOutput)
            registerUserProfileOutput(twitterOutput)
        }

    private class ValidTwitterHashTag : TwitterHashTag {
        override val isValid = true
        override val name = "TestName"
        override val accountUrl = "http://twitter.com"
    }

    class TestDispatchers : CoroutineDispatchers {
        override val main = ImmediatelyCoroutineDispatcher()
        override val io = ImmediatelyCoroutineDispatcher()
    }
}