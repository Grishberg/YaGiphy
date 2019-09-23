package com.github.grishberg.contentdetails

import com.github.grishberg.core.AnyCard

interface ContentDetails {
    fun onContentDetailsCardSelected(card: AnyCard)
    fun registerOutput(output: ContentDetailsOutput)
    fun unregisterOutput(output: ContentDetailsOutput)
}