package com.github.grishberg.giphygateway.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream


@WorkerThread
class ImageDownloader(
    private val client: OkHttpClient
) {
    /**
     * gets image from network.
     */
    fun downloadImage(url: String): Bitmap? {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        val body = response.body ?: return null
        val bufferedInputStream = BufferedInputStream(body.byteStream())
        return BitmapFactory.decodeStream(bufferedInputStream)
    }
}