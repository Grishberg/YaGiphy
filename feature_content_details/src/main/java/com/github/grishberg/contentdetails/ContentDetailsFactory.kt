package com.github.grishberg.contentdetails

import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Content

interface ContentDetailsFactory {
    fun createContent(selectedCard: AnyCard,
                      url: String,
                      userName: String,
                      twitterName: String?): Content
}