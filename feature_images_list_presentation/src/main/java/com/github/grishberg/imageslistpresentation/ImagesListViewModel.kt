package com.github.grishberg.imageslistpresentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListOutput

class ImagesListViewModel(
    private val cardsList: CardsList
) : ViewModel(), CardsListOutput {

    private val _cards = MutableLiveData<List<AnyCard>>()
    val cards: LiveData<List<AnyCard>>
        get() = _cards

    private val _updatedItemPosition = MutableLiveData<Int>()
    val updatedCardPosition: LiveData<Int>
        get() = _updatedItemPosition

    init {
        cardsList.registerOutput(this)
    }

    override fun updateCards(cards: List<Card<*>>) {
        _cards.value = cards
    }

    override fun updateCardByPosition(position: Int) {
        _updatedItemPosition.value = position
    }

    override fun onCleared() {
        cardsList.unregisterOutput(this)
    }
}