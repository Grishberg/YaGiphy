package com.github.grishberg.imageslistpresentation

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.core.Card
import com.github.grishberg.core.SingleLiveEvent
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListOutput

class ImagesListViewModel(
    private val cardsList: CardsList
) : ViewModel(), CardsListOutput {
    var imageListState: Parcelable? = null

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>>
        get() = _cards

    private val _updatedItemPosition = MutableLiveData<Int>()
    val updatedCardPosition: LiveData<Int>
        get() = _updatedItemPosition

    private val _onError = SingleLiveEvent<String>()
    val onError: LiveData<String>
        get() = _onError

    init {
        cardsList.registerOutput(this)
    }

    override fun updateCards(cards: List<Card>) {
        _cards.value = cards
    }

    override fun updateCardByPosition(position: Int) {
        _updatedItemPosition.value = position
    }

    override fun onCleared() {
        cardsList.unregisterOutput(this)
    }

    override fun showError(message: String) {
        _onError.value = message
    }
}