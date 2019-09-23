package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsFactory
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Content

class GiphyContentDetailsFactory(
    private val contentDetails: ContentDetails
) : ContentDetailsFactory {
    override fun createContent(
        selectedCard: AnyCard,
        url: String,
        userName: String,
        twitterName: String?
    ): Content {
        return GiphyContentDetails(selectedCard, url, userName, twitterName, contentDetails)
    }
}