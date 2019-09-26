package com.github.grishberg.imageslist

import android.graphics.Bitmap

/**
 * Contains image for card.
 */
interface ImageHolder {
    val bitmap: Bitmap

    object EMPTY : ImageHolder {
        override val bitmap: Bitmap
            get() = throw IllegalStateException("Empty image holder has no bitmap")
    }
}