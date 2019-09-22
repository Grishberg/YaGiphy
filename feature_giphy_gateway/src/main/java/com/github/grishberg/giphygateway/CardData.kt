package com.github.grishberg.giphygateway

import com.google.gson.annotations.SerializedName

data class Data(
    val data: List<CardData>,
    val pagination: Pagination
)

data class CardData(
    val type: String,
    val id: String,
    val url: String,
    val username: String,
    val title: String,
    val images: Images
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

data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    val count: Int,
    val offset: Int
)