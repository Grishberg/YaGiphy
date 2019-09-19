package com.github.grishberg.giphygateway

import android.graphics.Bitmap
import android.util.LruCache
import androidx.annotation.MainThread
import com.github.grishberg.core.Card
import com.github.grishberg.core.CardImageGateway
import com.github.grishberg.core.GetImageDelegate
import com.github.grishberg.giphygateway.api.ImageDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Or just use Picasso library.
 */
class CardsLruImageRepository(
    private val uiScope: CoroutineScope,
    private val imageDownloader: ImageDownloader,
    private val lruCache: LruCache<String, Bitmap>
) : CardImageGateway, GetImageDelegate {
    private val actions = mutableListOf<CardImageGateway.ImageReadyAction>()

    override fun requestImageForCard(card: Card) {
        card.requestImage(this)
    }

    override fun getImageByUrl(card: Card, imageId: String, url: String) {
        // 1) check from cache
        val bitmapFromCache = lruCache.get(imageId)
        if (bitmapFromCache != null) {
            notifyCardImageReceived(card)
            return
        }

        downloadImageFromNetwork(card, imageId, url)
    }

    private fun downloadImageFromNetwork(card: Card, imageId: String, url: String) =
        uiScope.launch {
            val task = async(Dispatchers.IO) {
                // background thread
                imageDownloader.downloadImage(url)
            }
            val bitmap = task.await()
            lruCache.put(imageId, bitmap)
            notifyCardImageReceived(card)
        }

    override fun registerImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.add(action)
    }

    override fun unreqisterImageReadyAction(action: CardImageGateway.ImageReadyAction) {
        actions.remove(action)
    }

    @MainThread
    private fun notifyCardImageReceived(card: Card) {
        for (action in actions) {
            action.onImageReadyForCard(card)
        }
    }

    companion object {
        /**
         * @param memClass = view.getMemoryClassFromActivity()
         * @param imageDownloader network image repository
         */
        fun create(
            uiScope: CoroutineScope,
            memClass: Int,
            imageDownloader: ImageDownloader
        ): CardImageGateway {
            val cacheSize = 1024 * 1024 * memClass / 8
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            return CardsLruImageRepository(uiScope, imageDownloader, bitmapCache)
        }
    }
}