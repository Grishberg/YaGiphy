package com.github.grishberg.contentdetailspresentation

import com.github.grishberg.contentdetails.CardInfoFactory
import com.github.grishberg.core.CardInfo

class CardInfoFactoryImpl : CardInfoFactory {
    override fun create(userName: String, displayName: String?): CardInfo {
        return CardInfoImpl(userName, displayName)
    }
}