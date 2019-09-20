package com.github.grishberg.core

interface GetImageDelegate {
    fun getImageByUrl(card: Card<*>, imageId: String, url: String)
}