package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.Content
import com.github.grishberg.contentdetails.ContentDetailsInput
import com.github.grishberg.core.AnyCard

class ContentRepository : ContentDetailsInput {
    override suspend fun requestContentDetails(selectedCard: AnyCard): Content {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}