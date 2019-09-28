package com.github.grishberg.giphygateway.api

import com.github.grishberg.giphygateway.Common
import com.github.grishberg.imageslist.CardFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.Test

private const val SINGLE_CARD_IMAGE_URL = "https://media0.giphy.com/media/3gNtpuuBVDnpzIaZTe/" +
        "480w_s.jpg?cid=9baa1adff90362481d89c6f9ef8dc660d149a6f628b1d1fe&rid=480w_s.jpg"

class GiphyApiTest {
    private val fakeInterceptor = FakeInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(fakeInterceptor)
        .build()

    private val cardFactory = mock<CardFactory>()

    private val underTest = GiphyApi("test", cardFactory, okHttpClient)

    @Test(expected = ResponseNotSuccessException::class)
    fun `throw exception when code not 200`() {
        fakeInterceptor.response = Common.createResponse("{}", 500)

        underTest.getTopCardList(0)
    }

    @Test
    fun `test single card`() {
        fakeInterceptor.response = Common.createResponse(
            Common.readTextFromFile("single_card_response.json"), 200
        )

        underTest.getCardById("id")

        verify(cardFactory).createCard(
            "3gNtpuuBVDnpzIaZTe",
            SINGLE_CARD_IMAGE_URL, "grandedame", "Grande Dame", "https://giphy.com/grandedame/"
        )
    }
}

internal class FakeInterceptor : Interceptor {
    var response: Response? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        return response!!
    }
}