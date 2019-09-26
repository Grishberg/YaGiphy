package com.github.grishberg.contentdetailspresentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsOutput
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.core.SingleLiveEvent
import com.github.grishberg.imageslist.CardInfo
import com.github.grishberg.imageslist.ImageHolder

class ContentDetailsViewModel(
    contentDetails: ContentDetails
) : ViewModel(), ContentDetailsOutput {
    private val _content = MutableLiveData<CardInfo>()
    val content: LiveData<CardInfo>
        get() = _content

    private val _needInvalidateImage = SingleLiveEvent<ImageHolder>()
    val needInvalidateImage: LiveData<ImageHolder>
        get() = _needInvalidateImage

    private val _showError = SingleLiveEvent<String>()
    val showError: LiveData<String>
        get() = _showError

    private val _twitterHashTag = MutableLiveData<TwitterHashTag>()
    val twitterHashTag: LiveData<TwitterHashTag>
        get() = _twitterHashTag

    private val _showStub = SingleLiveEvent<Boolean>()
    val showStub: LiveData<Boolean>
        get() = _showStub

    init {
        contentDetails.registerOutput(this)
    }

    override fun showTwitterHashTag(tag: TwitterHashTag) {
        _twitterHashTag.value = tag
    }

    override fun showCardDetails(cardInfo: CardInfo) {
        _content.value = cardInfo
    }

    override fun updateCardImage(image: ImageHolder) {
        _showStub.value = false
        _needInvalidateImage.value = image
    }

    override fun showStubImage() {
        _showStub.value = true
    }

    override fun showError(message: String) {
        _showError.value = message
    }
}