package com.github.grishberg.giphygateway

import com.github.grishberg.imageslist.Card
import com.github.grishberg.core.CoroutineDispatchers
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsListInput
import com.github.grishberg.imageslist.exceptions.CardListInputException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class CardsListGateway(
    private val uiScope: CoroutineScope,
    private val coroutineContextProvider: CoroutineDispatchers,
    private val giphyApi: GiphyApi
) : CardsListInput {

    override suspend fun requestTopCards(offset: Int): List<Card> {
        try {
            val data = uiScope.async(coroutineContextProvider.io) {
                giphyApi.getTopCardList(offset)
            }
            return data.await()
        } catch (e: Exception) {
            // convert giphy layer-specific errors to use-case specific.
            throw CardListInputException(e.message.orEmpty())
        }
    }
}