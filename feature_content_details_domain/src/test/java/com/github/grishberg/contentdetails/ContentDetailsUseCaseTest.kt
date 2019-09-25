package com.github.grishberg.contentdetails

import com.github.grishberg.contentdetails.exceptions.ContentDetailsInputException
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.core.CoroutineDispatchers
import com.github.grishberg.core.ImageHolder
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class ContentDetailsUseCaseTest {
    private val scope = CoroutineTestScope()

    private val coroutineContextProvider = TestContextProvider()
    private val twitterTag = ValidTwitterHashTag()
    private val downloadedImage = mock<ImageHolder>()
    private val card = mock<Card>()
    private val imageInput = mock<CardImageGateway>()
    private val contentDetailsInput = mock<ContentDetailsInput> {
        onBlocking { requestTwitterUserName(any()) } doReturn twitterTag
    }

    private val twitterOutput = mock<TwitterOutputBounds>()
    private val contentDetailsOutput = mock<ContentDetailsOutput>()

    private val underTest = createContentDetails()

    @Test
    fun `notify output card select event when card selected`() {
        underTest.onCardSelected(card)

        verify(contentDetailsOutput).showCardDetails(card)
    }

    @Test
    fun `receive twitter tag when card selected`() {
        underTest.onCardSelected(card)

        verify(contentDetailsOutput).showTwitterHashTag(twitterTag)
    }

    @Test
    fun `notify output image ready when image downloaded`() {
        underTest.onCardSelected(card)

        whenever(imageInput.requestImageForCard(card)) doReturn downloadedImage
        underTest.onImageReadyForCard(card)

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
    fun `notify error when twitter hashTag request failed`() {
        val contentDetailsInputWithError = mock<ContentDetailsInput> {
            onBlocking { requestCardById(any()) } doThrow ContentDetailsInputException("fail")
        }
        val underTest = createContentDetails(contentDetailsInputWithError)

        underTest.requestCardById("1234")

        verify(contentDetailsOutput).showError("fail")
    }

    private fun createContentDetails(contentDetails: ContentDetailsInput = contentDetailsInput) =
        ContentDetailsUseCase(scope, coroutineContextProvider, imageInput, contentDetails).apply {
            registerOutput(contentDetailsOutput)
            registerTwitterOutput(twitterOutput)
        }

    private class ValidTwitterHashTag : TwitterHashTag {
        override val isValid = true
        override val name = "TestName"
        override val accountUrl = "http://twitter.com"
    }

    class TestContextProvider : CoroutineDispatchers {
        override val main = ImmediatelyCoroutineDispatcher()
        override val io = ImmediatelyCoroutineDispatcher()
    }
}