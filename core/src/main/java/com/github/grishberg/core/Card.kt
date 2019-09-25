package com.github.grishberg.core


interface Card {
    val imageUrl: String
    val twitterUserName: String
    fun render(renderer: CardRenderer)
    fun isContentTheSame(card: Card): Boolean
    fun handleClick()
    fun provideCardInfo(): CardInfo
    fun openUserProfile(openUrlDelegate: (String) -> Unit)
}