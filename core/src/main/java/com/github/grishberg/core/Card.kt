package com.github.grishberg.core

interface Card<R : Renderer> {
    fun render(renderer: R)
}