package com.github.grishberg.giphygateway

import android.graphics.Bitmap
import com.github.grishberg.imageslist.ImageHolder

inline class BitmapHolder(
    override val bitmap: Bitmap
) : ImageHolder