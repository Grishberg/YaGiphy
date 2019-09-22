package com.github.grishberg.giphygateway

import com.github.grishberg.core.AnyCard
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsListInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class CardsListGateway(
    private val uiScope: CoroutineScope,
    private val giphyApi: GiphyApi
) : CardsListInput {
    private val actions = mutableListOf<CardsListInput.CardsReceivedAction>()

    constructor(uiScope: CoroutineScope, apiKey: String) :
            this(uiScope, GiphyApi(apiKey, OkHttpClient()))

    fun setCardFactory(cardFactory: CardFactory) {
        giphyApi.cardFactory = cardFactory
    }

    override fun requestTopCards(offset: Int) {
        uiScope.launch {
            val task = async(Dispatchers.IO) {
                // background thread
                giphyApi.getTopCardList(offset)
            }
            val cardList = task.await()
            notifyCardListReceived(cardList)
        }
    }

    override fun registerCardsReceivedAction(action: CardsListInput.CardsReceivedAction) {
        actions.add(action)
    }

    override fun unRegisterCardsReceivedAction(action: CardsListInput.CardsReceivedAction) {
        actions.add(action)
    }

    private fun notifyCardListReceived(cards: List<AnyCard>) {
        for (action in actions) {
            action.onCardsReceived(cards)
        }
    }
}