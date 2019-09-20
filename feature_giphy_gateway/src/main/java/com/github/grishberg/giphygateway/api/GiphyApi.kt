package com.github.grishberg.giphygateway.api

import androidx.annotation.WorkerThread
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardFactory
import com.github.grishberg.giphygateway.CardData
import com.github.grishberg.giphygateway.Data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


private const val TREND_ENDPOINT = "https://api.giphy.com/v1/gifs/trending"

@WorkerThread
class GiphyApi(
    private val apiKey: String,
    private val client: OkHttpClient,
    private val cardFactory: CardFactory
) {
    private val gson:Gson
    init {
        val gsonBuilder = GsonBuilder()
        // register type adapters here, specify field naming policy, etc.
        gson = gsonBuilder.create()
    }

    fun getTopCardList(offset: Int): List<Card<*>> {

        val httpBuider = TREND_ENDPOINT.toHttpUrlOrNull()!!.newBuilder()
        httpBuider.addQueryParameter("api_key", apiKey)
        val request = Request.Builder()
            .url(httpBuider.build())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return emptyList()
        val stream = body.charStream()
        val cardDataType = object : TypeToken<Data>(){}.type
        val cardDataList = gson.fromJson<Data>(stream, cardDataType)

        val result = mutableListOf<Card<*>>()
        for (cardData in cardDataList.data) {
            result.add(cardFactory.createCard(cardData.id, cardData.url))
        }
        return result
    }
}