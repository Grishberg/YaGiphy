package com.github.grishberg.giphygateway.api

import com.github.grishberg.imageslist.CardFactory
import com.nhaarman.mockitokotlin2.mock
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test

class GiphyApiTest {
    private val fakeInterceptor = FakeInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(fakeInterceptor)
        .build()

    private val mockFactory = mock<CardFactory>()
    private var request = Request.Builder()
        .url("https://twitter.com/users/username_available")
        .build()

    private val underTest = GiphyApi("test", okHttpClient, mockFactory)

    @Test(expected = ResponseNotSuccessException::class)
    fun `throw exception when code not 200`() {
        fakeInterceptor.response = createResponse("{}", 500)

        underTest.getTopCardList(0)
    }

    private fun createResponse(body: String, code: Int = 200): Response =
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("Intercepted!")
            .body(body.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))
            .build()
}

internal class FakeInterceptor : Interceptor {
    var response: Response? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        return response!!
    }
}