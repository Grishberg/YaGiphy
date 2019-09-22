package com.github.grishberg.giphygateway.api

import androidx.annotation.WorkerThread
import com.github.grishberg.core.AnyCard
import com.github.grishberg.giphygateway.Data
import com.github.grishberg.imageslist.CardFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request


private const val TREND_ENDPOINT = "https://api.giphy.com/v1/gifs/trending"
private const val LIMIT = 10

@WorkerThread
class GiphyApi(
    private val apiKey: String,
    private val client: OkHttpClient,
    internal var cardFactory: CardFactory = CardFactory.STUB
) {
    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        // register type adapters here, specify field naming policy, etc.
        gson = gsonBuilder.create()
    }

    fun getTopCardList(offset: Int): List<AnyCard> {

        val httpBuilder = TREND_ENDPOINT.toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("api_key", apiKey)
        httpBuilder.addQueryParameter("offset", offset.toString())
        httpBuilder.addQueryParameter("limit", LIMIT.toString())
        val request = Request.Builder()
            .url(httpBuilder.build())
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return emptyList()
        val stream = body.charStream()
        val cardDataType = object : TypeToken<Data>() {}.type
        val cardDataList = gson.fromJson<Data>(stream, cardDataType)

        val result = mutableListOf<AnyCard>()
        for (cardData in cardDataList.data) {
            result.add(
                cardFactory.createCard(
                    cardData.id,
                    cardData.url,
                    cardData.images.fixedHeightStill.url
                )
            )
        }
        return result
    }
}