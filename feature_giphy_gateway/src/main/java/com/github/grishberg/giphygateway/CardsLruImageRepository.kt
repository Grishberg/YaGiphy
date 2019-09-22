package com.github.grishberg.giphygateway

import android.graphics.Bitmap
import android.util.LruCache
import androidx.annotation.MainThread
import com.github.grishberg.core.AnyCard
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.core.GetImageDelegate
import com.github.grishberg.giphygateway.api.ImageDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

/**
 * Or just use Picasso library.
 */
class CardsLruImageRepository(
    private val uiScope: CoroutineScope,
    private val imageDownloader: ImageDownloader,
    private val lruCache: LruCache<String, Bitmap>
) : CardImageGateway, GetImageDelegate {
    private val actions = mutableListOf<CardImageGateway.ImageReadyAction>()

    override fun requestImageForCard(card: AnyCard): Bitmap? {
        return card.requestImage(this)
    }

    override fun getImageByUrl(card: Card<*>, url: String): Bitmap? {
        // 1) check from cache
        val bitmapFromCache = lruCache.get(url)
        if (bitmapFromCache != null) {
            return bitmapFromCache
        }

        downloadImageFromNetwork(card, url)
        return null
    }

    private fun downloadImageFromNetwork(card: AnyCard, url: String) =
        uiScope.launch {
            val task = async(Dispatchers.IO) {
                // background thread
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
    private fun notifyCardImageReceived(card: Card<*>) {
        for (action in actions) {
            action.onImageReadyForCard(card)
        }
    }

    companion object {
        /**
         * @param uiScope CoroutineScope for controlling coroutines.
         * @param memClass = view.getMemoryClassFromActivity()
         * @param okHttpClient OkHttpClient instance
         */
        fun create(
            uiScope: CoroutineScope,
            memClass: Int
        ): CardImageGateway {
            val okHttpClient = OkHttpClient()
            val imageDownloader = ImageDownloader(okHttpClient)
            val cacheSize = 1024 * 1024 * memClass / 8
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            return CardsLruImageRepository(uiScope, imageDownloader, bitmapCache)
        }
    }
}