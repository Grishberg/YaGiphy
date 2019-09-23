package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetailsInput {
    suspend fun requestTwitterUserName(selectedCard: Card): TwitterHashTag
}