package com.github.grishberg.contentdetails

import com.github.grishberg.core.AnyCard

interface ContentDetailsInput {
    suspend fun requestContentDetails(selectedCard: AnyCard): Content
}