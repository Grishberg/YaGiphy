package com.github.grishberg.giphygateway.api

import androidx.annotation.WorkerThread
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardFactory
import com.github.grishberg.giphygateway.CardData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request


private val TREND_ENDPOINT = "api.giphy.com/v1/gifs/trending"

@WorkerThread
class GiphyApi(
    private val apiKey: String,
    private val client: OkHttpClient,
    private val cardFactory: CardFactory
) {
    private val gson = Gson()

    fun getTopCardList(offset: Int): List<Card> {
        val cardDataType = object : TypeToken<ArrayList<CardData>>() {}.type

        val request = Request.Builder()
            .url(TREND_ENDPOINT)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return emptyList()
        val stream = body.charStream()
        val cardDataList = gson.fromJson<List<CardData>>(stream, cardDataType)

        val result = mutableListOf<Card>()
        for (cardData in cardDataList) {
            result.add(cardFactory.createCard())
        }
        return result
    }
}