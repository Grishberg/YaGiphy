package com.github.grishberg.imageslist


interface Card {
    val imageUrl: String
    val twitterUserName: String
    fun render(renderer: CardRenderer)
    fun isContentTheSame(card: Card): Boolean
    fun provideCardInfo(): CardInfo
    fun openUserProfile(openUrlDelegate: (String) -> Unit)
}