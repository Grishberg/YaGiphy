package com.github.grishberg.core

typealias AnyCard = Card<*>

interface Card<R : CardRenderer> {
    val imageUrl: String
    val twitterUserName: String
    fun render(renderer: R)
    fun isContentTheSame(card: AnyCard): Boolean
    fun handleClick()
    fun createContent(twitterValid: Boolean): AnyContent
}