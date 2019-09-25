package com.github.grishberg.giphygateway

import android.graphics.Bitmap
import android.util.LruCache
import androidx.annotation.MainThread
import com.github.grishberg.core.*
import com.github.grishberg.giphygateway.api.ImageDownloader
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

/**
 * Or just use Picasso library.
 */
class CardsLruImageRepository(
    private val uiScope: CoroutineScope,
    private val coroutineContextProvider: CoroutineDispatchers,
    private val imageDownloader: ImageDownloader,
    private val lruCache: LruCache<String, Bitmap>
) : CardImageGateway {
    private val actions = mutableListOf<CardImageGateway.ImageReadyAction>()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        uiScope.launch(Dispatchers.Main) {
            actions.forEach { it.onImageRequestError(exception.message.orEmpty()) }
        }
    }

    override fun requestImageForCard(card: Card): ImageHolder {
        val imageHolder = getImageOrEmptyHolder(card)
        if (imageHolder == ImageHolder.EMPTY) {
            downloadImageFromNetwork(card, card.imageUrl)
        }
        return imageHolder
    }

    override fun getImageOrEmptyHolder(card: Card): ImageHolder {
        val bitmapFromCache = lruCache.get(card.imageUrl)
        if (bitmapFromCache != null) {
            return BitmapHolder(bitmapFromCache)
        }
        return ImageHolder.EMPTY
    }

    private fun downloadImageFromNetwork(card: Card, url: String) =
        uiScope.launch(errorHandler) {
            val task = async(coroutineContextProvider.io) {
                imageDownloader.downloadImage(url)
            }
            val bitmap = task.await()
            if (bitmap != null) {
                lruCache.put(url, bitmap)
                notifyCardImageReceived(card)
            }
        }

    override fun registerImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.add(action)
    }

    override fun unregisterImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.remove(action)
    }

    @MainThread
    private fun notifyCardImageReceived(card: Card) {
        actions.forEach { it.onImageReadyForCard(card) }
    }

    companion object {
        fun create(
            uiScope: CoroutineScope,
            coroutineContextProvider: CoroutineDispatchers,
            memClass: Int
        ): CardImageGateway {
            val okHttpClient = OkHttpClient()
            val imageDownloader = ImageDownloader(okHttpClient)
            val cacheSize = 1024 * 1024 * memClass / 8
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            return CardsLruImageRepository(
                uiScope,
                coroutineContextProvider,
                imageDownloader,
                bitmapCache
            )
        }
    }
}