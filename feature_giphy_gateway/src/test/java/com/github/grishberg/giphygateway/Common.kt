package com.github.grishberg.giphygateway

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class Common {
    companion object {
        fun readTextFromFile(fn: String): String =
            Common::class.java.getResource("/$fn")!!.readText()


        fun createResponse(body: String, code: Int = 200): Response =
            Response.Builder()
                .request(
                    Request.Builder()
                        .url("https://localhost")
                        .build()
                )
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("Intercepted!")
                .body(body.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .build()
    }
}
