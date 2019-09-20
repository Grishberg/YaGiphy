package com.github.grishberg.imageslistpresentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.CardsList
import com.github.grishberg.imageslist.CardsListOutput

class ImagesListViewModel(
    private val cardsList: CardsList
) : ViewModel(), CardsListOutput {

    private val _cards = MutableLiveData<List<Card<*>>>()
    val cards: LiveData<List<Card<*>>>
        get() = _cards

    init {
        cardsList.registerOutput(this)
    }

    override fun updateCards(cards: List<Card<*>>) {
        _cards.value = cards
    }

    override fun onCleared() {
        cardsList.unregisterOutput(this)
    }
}