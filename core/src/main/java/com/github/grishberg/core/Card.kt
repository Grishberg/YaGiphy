package com.github.grishberg.core

typealias AnyCard = Card<*>

interface Card<R : CardRenderer> {
    fun requestImage(delegate: GetImageDelegate)
    fun render(renderer: R)
    fun isContentTheSame(card: AnyCard): Boolean
}