package com.github.grishberg.core

import android.graphics.Bitmap

interface CardImageGateway {
    interface ImageReadyAction {
        /**
         * Is called when image for {@param targetCard} is ready.
         */
        fun onImageReadyForCard(targetCard: AnyCard)
    }

    fun requestImageForCard(card: AnyCard): Bitmap?
    fun registerImageReadyAction(action: ImageReadyAction)
    fun unregisterImageReadyAction(action: ImageReadyAction)
}