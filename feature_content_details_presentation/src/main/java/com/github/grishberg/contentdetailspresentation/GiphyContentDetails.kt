package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Content
import com.github.grishberg.core.ContentRenderer

class GiphyContentDetails(
    private val card: AnyCard,
    private val url: String,
    private val userName: String,
    private val twitterName: String?,
    private val contentDetails: ContentDetails
) : Content {

    override fun render(renderer: ContentRenderer) {
        if (twitterName != null) {
            renderer.showTwitterHashTitle("@$twitterName")
        }
        val bitmap = contentDetails.getImageForUrl(card)
        if (bitmap == null) {
            renderer.showImageStub()
            return
        }

        renderer.showImage(bitmap)
    }
}