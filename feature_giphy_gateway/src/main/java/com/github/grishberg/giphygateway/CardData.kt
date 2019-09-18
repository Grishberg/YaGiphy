package com.github.grishberg.giphygateway

import com.google.gson.annotations.SerializedName

data class CardData(
    val type: String,
    val id: String,
    val url: String,
    val username: String,
    val title: String,
    val image: Images
)

data class Images(
    @SerializedName("fixed_height_still")
    val fixedHeightStill: FixedHighStillImage
)

data class FixedHighStillImage(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int
)