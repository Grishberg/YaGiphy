package com.github.grishberg.giphygateway

import com.github.grishberg.core.Card
import com.github.grishberg.giphygateway.api.GiphyApi
import com.github.grishberg.imageslist.CardFactory
import com.github.grishberg.imageslist.CardsListInput
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class CardsListGateway(
    private val uiScope: CoroutineScope,
    private val giphyApi: GiphyApi
) : CardsListInput {
    private val actions = mutableListOf<CardsListInput.CardsReceivedAction>()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        uiScope.launch(Dispatchers.Main) {
            notifyError(exception.message)
        }
    }

    constructor(uiScope: CoroutineScope, apiKey: String) :
            this(uiScope, GiphyApi(apiKey, OkHttpClient()))

    fun setCardFactory(cardFactory: CardFactory) {
        giphyApi.cardFactory = cardFactory
    }

    override fun requestTopCards(offset: Int) {
        uiScope.launch(errorHandler) {
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

    private fun notifyCardListReceived(cards: List<Card>) {
        for (action in actions) {
            action.onCardsReceived(cards)
        }
    }

    private fun notifyError(message: String?) {
        for (action in actions) {
            action.onError(message.orEmpty())
        }
    }

}