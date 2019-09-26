package com.github.grishberg.giphygateway.api

import com.google.gson.annotations.SerializedName

/**
 * Giphy json POJOs
 */
internal data class CardListData(
    val data: List<CardData>,
    val pagination: Pagination
)

internal data class SingleCardData(
    val data: CardData
)

internal data class CardData(
    val type: String,
    val id: String,
    val url: String,
    val title: String,
    val user: User?,
    val username: String,
    val images: Images
)

internal data class User(
    val username: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("profile_url")
    val profileUrl: String
)

internal data class Images(
    @SerializedName("480w_still")
    val previewImage: PreviewImage
)

internal data class PreviewImage(
    val url: String,
    val width: Int,
    val height: Int
)

internal data class Pagination(
    @SerializedName("total_count")
    val totalCount: Int,
    val count: Int,
    val offset: Int
)