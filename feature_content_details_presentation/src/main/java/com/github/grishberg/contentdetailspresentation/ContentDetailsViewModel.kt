package com.github.grishberg.contentdetailspresentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.grishberg.contentdetails.Content
import com.github.grishberg.contentdetails.ContentDetails
import com.github.grishberg.contentdetails.ContentDetailsOutput
import com.github.grishberg.core.SingleLiveEvent

class ContentDetailsViewModel(
    private val contentDetails: ContentDetails
) : ViewModel(), ContentDetailsOutput {
    private val _content = MutableLiveData<Content>()
    val content: LiveData<Content>
        get() = _content

    private val _needInvalidateImage = MutableLiveData<Boolean>()
    val needInvalidateImage: LiveData<Boolean>
        get() = _needInvalidateImage


    private val _showError = SingleLiveEvent<String>()
    val showError: LiveData<String>
        get() = _showError

    override fun showContentDetails(content: Content) {
        _content.value = content
    }

    override fun updateCardImage() {
        _needInvalidateImage.value = true
    }

    override fun showError(message: String) {
        _showError.value = message
    }
}