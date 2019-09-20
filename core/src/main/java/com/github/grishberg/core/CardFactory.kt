package com.github.grishberg.core

interface CardFactory {
    fun createCard(id: String, url: String) : Card<*>
}