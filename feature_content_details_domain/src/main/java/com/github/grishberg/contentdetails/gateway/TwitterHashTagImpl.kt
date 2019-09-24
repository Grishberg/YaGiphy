package com.github.grishberg.contentdetails.gateway

import com.github.grishberg.contentdetails.TwitterHashTag

class TwitterHashTagImpl(
    override val isValid: Boolean,
    override val name: String,
    override val accountUrl: String
) : TwitterHashTag