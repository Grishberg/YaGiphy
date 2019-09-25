package com.github.grishberg.giphygateway.api

import androidx.annotation.WorkerThread
import com.github.grishberg.core.Card
import com.github.grishberg.giphygateway.CardListData
import com.github.grishberg.giphygateway.SingleCardData
import com.github.grishberg.imageslist.CardFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

private const val TREND_ENDPOINT = "https://api.giphy.com/v1/gifs/trending"
private const val SINGLE_GIF_ENDPOINT = "https://api.giphy.com/v1/gifs"
private const val LIMIT = 20

@WorkerThread
class GiphyApi(
    private val apiKey: String,
    private val client: OkHttpClient,
    internal var cardFactory: CardFactory = CardFactory.STUB
) {
    constructor(apiKey: String) : this(apiKey, OkHttpClient())

    private val gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        // register type adapters here, specify field naming policy, etc.
        gson = gsonBuilder.create()
    }

    @Throws(ResponseNotSuccessException::class)
    fun getTopCardList(offset: Int): List<Card> {
        val httpBuilder = TREND_ENDPOINT.toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("api_key", apiKey)
        httpBuilder.addQueryParameter("offset", offset.toString())
        httpBuilder.addQueryParameter("limit", LIMIT.toString())
        val request = Request.Builder()
            .url(httpBuilder.build())
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw ResponseNotSuccessException("Could not receive data\n response code is ${response.code}")
        }
        val body = response.body ?: return emptyList()
        val stream = body.charStream()
        val cardDataType = object : TypeToken<CardListData>() {}.type
        val cardDataList = gson.fromJson<CardListData>(stream, cardDataType)

        val result = mutableListOf<Card>()
        for (cardData in cardDataList.data) {
            result.add(
                cardFactory.createCard(
                    cardData.id,
                    cardData.images.previewImage.url,
                    cardData.username
                )
            )
        }
        return result
    }


    @Throws(ResponseNotSuccessException::class)
    fun getCardById(id: String): Card {
        val httpBuilder = SINGLE_GIF_ENDPOINT.toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("api_key", apiKey)
        httpBuilder.addPathSegments(id)

        val request = Request.Builder()
            .url(httpBuilder.build())
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw ResponseNotSuccessException(
                "Could not receive data" +
                        "\nresponse code is ${response.code}"
            )
        }
        val body = response.body ?: throw ResponseNotSuccessException(
            "Could not receive data:" +
                    "\nserver returns nothing"
        )
        val stream = body.charStream()
        val cardDataType = object : TypeToken<SingleCardData>() {}.type
        val cardData = gson.fromJson<SingleCardData>(stream, cardDataType)

        return cardFactory.createCard(
            cardData.data.id,
            cardData.data.images.previewImage.url,
            cardData.data.username
        )
    }
}