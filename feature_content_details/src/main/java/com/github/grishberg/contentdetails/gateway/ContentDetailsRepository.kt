package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.Content
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsInput
import com.github.grishberg.core.AnyCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContentDetailsRepository(
    private val uiScope: CoroutineScope,
    private val twitterApi: TwitterApi
) : ContentDetailsInput {

    override suspend fun requestContentDetails(selectedCard: AnyCard): Content =
        suspendCoroutine { cont ->
            someLongComputation(params) { cont.resume(it) }
        }
    selectedCard.checkTwitterName()
        uiScope.launch {
            val task = async(Dispatchers.IO) {
                val isVa
            }

        }
    }
}