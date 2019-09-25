package com.github.grishberg.giphygateway

import com.google.gson.annotations.SerializedName

/**
 * Giphy json POJOs
 */
data class CardListData(
    val data: List<CardData>,
    val pagination: Pagination
)

data class SingleCardData(
    val data: CardData
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
    @SerializedName("480w_still")
    val previewImage: PreviewImage
)

data class PreviewImage(
    val url: String,
    val width: Int,
    val height: Int
)

data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    val count: Int,
    val offset: Int
)