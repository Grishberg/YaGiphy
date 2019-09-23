package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.core.Card
import com.github.grishberg.core.Content
import com.github.grishberg.core.ContentRenderer

class GiphyContentDetails(
    private val card: Card,
    private val url: String,
    private val userName: String,
    private val twitterHashTag: TwitterHashTag,
    private val contentDetails: ContentDetails
) : Content {

    override fun render(renderer: ContentRenderer) {
        if (twitterHashTag.isValid) {
            renderer.showTwitterHashTitle(twitterHashTag.name)
        }
        val bitmap = contentDetails.getImageForUrl(card)
        if (bitmap == null) {
            renderer.showImageStub()
            return
        }

        renderer.showImage(bitmap)
    }
}