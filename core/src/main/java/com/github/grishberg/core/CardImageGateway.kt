package com.github.grishberg.core

interface CardImageGateway {
    interface ImageReadyAction {
        /**
         * Is called when image for {@param targetCard} is ready.
         */
        fun onImageReadyForCard(targetCard: Card)
    }

    fun requestImageForCard(card: Card)
    fun registerImageReadyAction(action: ImageReadyAction)
    fun unreqisterImageReadyAction(action: ImageReadyAction)
}