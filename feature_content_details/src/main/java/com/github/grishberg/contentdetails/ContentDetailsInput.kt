package com.github.grishberg.contentdetails

import com.github.grishberg.contentdetails.exceptions.ContentDetailsInputException
import com.github.grishberg.imageslist.Card

interface ContentDetailsInput {
    suspend fun requestTwitterUserName(selectedCard: Card): TwitterHashTag

    @Throws(ContentDetailsInputException::class)
    suspend fun requestCardById(id: String): Card
}