package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.TwitterHashTag
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

private const val USER_EXISTS_TWITTER_JSON = "{'valid'=false, 'reason'='taken'}"
private const val USER_NOT_VALID_TWITTER_JSON = "{'valid'=true, 'reason'='available'}"
private const val TEST_NAME = "testName"

class TwitterApiTest {
    private val fakeInterceptor = FakeInterceptor()
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(fakeInterceptor)
        .build()
    private var request = Request.Builder()
        .url("https://twitter.com/users/username_available")
        .build()
    private val underTest = TwitterApi(okHttpClient)

    @Test
    fun `valid twitter name when exists`() {
        fakeInterceptor.response = createResponse(USER_EXISTS_TWITTER_JSON)

        val hashTag = underTest.requestValidTwitterHandle(TEST_NAME)

        assertEquals("@$TEST_NAME", hashTag.name)
    }

    @Test
    fun `empty twitter name when not exists`() {
        fakeInterceptor.response = createResponse(USER_NOT_VALID_TWITTER_JSON)

        val hashTag = underTest.requestValidTwitterHandle(TEST_NAME)

        assertEquals(TwitterHashTag.EMPTY, hashTag)
    }

    @Test
    fun `empty twitter hash tag when 500 response`() {
        fakeInterceptor.response = createResponse("{}", 500)

        val hashTag = underTest.requestValidTwitterHandle(TEST_NAME)

        assertEquals(TwitterHashTag.EMPTY, hashTag)
    }

    private fun createResponse(body: String, code: Int = 200): Response =
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("Intercepted!")
            .body(body.toResponseBody("text/plain; charset=utf-8".toMediaTypeOrNull()))
            .build()
}

internal class FakeInterceptor : Interceptor {
    var response: Response? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        return response!!
    }
}