package com.github.grishberg.contentdetails

import android.graphics.Bitmap
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContentDetailsUseCaseTest {
    @get:Rule
    val scope = CoroutineTestRule()

    private val twitterTag = ValidTwitterHashTag()

    private val card = mock<Card>()
    private val imageInput = mock<CardImageGateway>()
    private val contentDetailsInput = mock<ContentDetailsInput> {
        onBlocking { requestTwitterUserName(any()) } doReturn twitterTag
    }
    private val underTest = ContentDetailsUseCase(scope, imageInput, contentDetailsInput)

    private val twitterOutput = mock<TwitterOutputBounds>()
    private val contentDetailsOutput = mock<ContentDetailsOutput>()

    @Before
    fun setUp() {
        underTest.registerOutput(contentDetailsOutput)
        underTest.registerTwitterOutput(twitterOutput)
    }

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
        /*
        underTest.onCardSelected(card)

        whenever(imageInput.requestImageForCard())
        underTest.onImageReadyForCard(card)

        verify(contentDetailsOutput).updateCardImage(twitterTag)
         */
    }
    private class ValidTwitterHashTag : TwitterHashTag {
        override val isValid = true
        override val name = "TestName"
        override val accountUrl = "http://twitter.com"
    }
}