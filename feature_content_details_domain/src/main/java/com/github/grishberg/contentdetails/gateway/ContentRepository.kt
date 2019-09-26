package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.ContentDetailsInput
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.contentdetails.exceptions.ContentDetailsInputException
import com.github.grishberg.core.CoroutineDispatchers
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imageslist.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient

class ContentRepository(
    private val uiScope: CoroutineScope,
    private val coroutineContextProvider: CoroutineDispatchers,
    private val twitterApi: TwitterApi,
    private val giphyApi: GiphyApi
) : ContentDetailsInput {

    override suspend fun requestTwitterUserName(selectedCard: Card): TwitterHashTag {
        val task = uiScope.async(coroutineContextProvider.io) {
            twitterApi.requestValidTwitterHandle(selectedCard.twitterUserName)
        }
        return task.await()
    }

    override suspend fun requestCardById(id: String): Card {
        try {
            val data = uiScope.async(coroutineContextProvider.io) { giphyApi.getCardById(id) }
            return data.await()
        } catch (e: Exception) {
            throw ContentDetailsInputException(e.message.orEmpty())
        }
    }

    companion object {
        fun create(
            uiScope: CoroutineScope,
            coroutineContextProvider: CoroutineDispatchers,
            giphyApi: GiphyApi
        ): ContentRepository {
            return ContentRepository(
                uiScope,
                coroutineContextProvider,
                TwitterApi(OkHttpClient()),
                giphyApi
            )
        }
    }
}