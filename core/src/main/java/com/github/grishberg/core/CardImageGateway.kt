package com.github.grishberg.core

interface CardImageGateway: ImagesProvider {
    interface ImageReadyAction {
        /**
         * Is called when image for {@param targetCard} is ready.
         */
        fun onImageReadyForCard(targetCard: Card)

        fun onImageRequestError(message: String)
    }

    fun requestImageForCard(card: Card): ImageHolder
    fun registerImageReadyAction(action: ImageReadyAction)
    fun unregisterImageReadyAction(action: ImageReadyAction)
}