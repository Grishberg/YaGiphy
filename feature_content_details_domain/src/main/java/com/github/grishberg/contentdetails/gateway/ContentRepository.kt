package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.ContentDetailsInput
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.core.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient

class ContentRepository(
    private val uiScope: CoroutineScope,
    private val twitterApi: TwitterApi
) : ContentDetailsInput {

    override suspend fun requestTwitterUserName(selectedCard: Card): TwitterHashTag {
        val task = uiScope.async(Dispatchers.IO) {
            twitterApi.isValidTwitterHandle(selectedCard.twitterUserName)
        }

        return task.await()
    }

    companion object {
        fun create(uiScope: CoroutineScope): ContentRepository {
            return ContentRepository(uiScope, TwitterApi(OkHttpClient()))
        }
    }
}