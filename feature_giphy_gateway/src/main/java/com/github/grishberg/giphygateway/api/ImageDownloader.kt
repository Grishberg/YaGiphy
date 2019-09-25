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
    @Throws(GiphyImageReceiveException::class)
    fun downloadImage(url: String): Bitmap? {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw GiphyImageReceiveException("Response code is ${response.code}")
        }
        val body =
            response.body ?: throw GiphyImageReceiveException("Response body is empty")
        val bufferedInputStream = BufferedInputStream(body.byteStream())
        return BitmapFactory.decodeStream(bufferedInputStream)
    }
}