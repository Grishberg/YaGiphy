package com.github.grishberg.core

interface Card {
    fun requestImage(delegate: GetImageDelegate)
    fun render()
}