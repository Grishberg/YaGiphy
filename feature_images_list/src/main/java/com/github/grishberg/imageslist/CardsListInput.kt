package com.github.grishberg.imageslist

import com.github.grishberg.core.Card
import com.github.grishberg.imageslist.exceptions.CardListInputException

interface CardsListInput {
    @Throws(CardListInputException::class)
    suspend fun requestTopCards(offset: Int): List<Card>
}