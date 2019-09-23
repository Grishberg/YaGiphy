package com.github.grishberg.contentdetails.gateway

import androidx.annotation.WorkerThread
import com.github.grishberg.contentdetails.TwitterHashTag
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request


private const val TWITTER_ENDPOINT =
    "https://twitter.com/users/username_available?username=#{handle.substr(1)}"
private const val TAKEN = "taken"

@WorkerThread
class TwitterApi(
    private val client: OkHttpClient
) {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        // register type adapters here, specify field naming policy, etc.
        gson = gsonBuilder.create()
    }

    /**
     * Returns true if given {@param userName} is existing twitter user name.
     */
    fun isValidTwitterHandle(userName: String): TwitterHashTag {
        val httpBuilder = TWITTER_ENDPOINT.toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("username", userName)
        val request = Request.Builder()
            .url(httpBuilder.build())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return TwitterHashTag.EMPTY_HASHTAG
        val stream = body.charStream()
        val cardDataType = object : TypeToken<TwitterValidationData>() {}.type
        val validationData = gson.fromJson<TwitterValidationData>(stream, cardDataType)
        if (validationData.valid || validationData.reason != TAKEN) {
            return TwitterHashTag.EMPTY_HASHTAG
        }
        return TwitterHashTagImpl(true, "@$userName", "https://twitter.com/$userName")
    }
}