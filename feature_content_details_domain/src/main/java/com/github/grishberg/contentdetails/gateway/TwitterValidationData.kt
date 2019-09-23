package com.github.grishberg.contentdetails.gateway

data class TwitterValidationData(
    val valid: Boolean,
    val reason: String // taken/available
)