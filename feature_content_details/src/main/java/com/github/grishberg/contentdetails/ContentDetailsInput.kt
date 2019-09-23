package com.github.grishberg.contentdetails

import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.AnyContent

interface ContentDetailsInput {
    suspend fun requestContentDetails(selectedCard: AnyCard): AnyContent
}