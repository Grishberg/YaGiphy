package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.ContentDetailsInput
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient

class ContentRepository(
    private val uiScope: CoroutineScope,
    private val twitterApi: TwitterApi
) : ContentDetailsInput {

    override suspend fun requestContentDetails(selectedCard: AnyCard): Content {
        val task = uiScope.async(Dispatchers.IO) {
            twitterApi.isValidTwitterHandle(selectedCard.twitterUserName)
        }

        val isValidTwitter = task.await()
        return selectedCard.createContent(isValidTwitter)
    }

    companion object {
        fun create(uiScope: CoroutineScope): ContentRepository {
            return ContentRepository(uiScope, TwitterApi(OkHttpClient()))
        }
    }
}