package com.github.grishberg.core

import android.graphics.Bitmap

interface CardImageGateway {
    interface ImageReadyAction {
        /**
         * Is called when image for {@param targetCard} is ready.
         */
        fun onImageReadyForCard(targetCard: Card)
        fun onError(message: String)
    }

    fun requestImageForCard(card: Card): Bitmap?
    fun registerImageReadyAction(action: ImageReadyAction)
    fun unregisterImageReadyAction(action: ImageReadyAction)
}