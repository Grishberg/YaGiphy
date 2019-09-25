package com.github.grishberg.contentdetailspresentation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsOutput
import com.github.grishberg.contentdetails.TwitterHashTag
import com.github.grishberg.core.Card
import com.github.grishberg.core.SingleLiveEvent

class ContentDetailsViewModel(
    contentDetails: ContentDetails
) : ViewModel(), ContentDetailsOutput {
    private val _content = MutableLiveData<Card>()
    val content: LiveData<Card>
        get() = _content

    private val _needInvalidateImage = SingleLiveEvent<Bitmap>()
    val needInvalidateImage: LiveData<Bitmap>
        get() = _needInvalidateImage

    private val _showError = SingleLiveEvent<String>()
    val showError: LiveData<String>
        get() = _showError

    private val _twitterHashTag = MutableLiveData<TwitterHashTag>()
    val twitterHashTag: LiveData<TwitterHashTag>
        get() = _twitterHashTag

    init {
        contentDetails.registerOutput(this)
    }

    override fun showTwitterHashTag(tag: TwitterHashTag) {
        _twitterHashTag.value = tag
    }

    override fun showCardDetails(card: Card) {
        _content.value = card
    }

    override fun updateCardImage(image: Bitmap) {
        _needInvalidateImage.value = image
    }

    override fun showError(message: String) {
        _showError.value = message
    }
}