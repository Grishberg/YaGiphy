package com.github.grishberg.contentdetails

import com.github.grishberg.core.Card

interface ContentDetails {
    fun onContentDetailsCardSelected(card: Card<*>)
    fun onBackPressed()
}