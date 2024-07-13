package ru.netology.singlealbum.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.singlealbum.dto.Album
import java.io.IOException
import java.util.concurrent.TimeUnit

class AlbumRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    companion object {
        private const val BASE_URL = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
        private val jsonType = "application/json".toMediaType()
    }
    private fun <T> baseRequest(
        callback: PostCallback<T>,
        typeToken: TypeToken<T>,
        requestBuilder: Request.Builder.() -> Unit,
    ) {
        val builder = Request.Builder()
        builder.requestBuilder()
        val request = builder.build()
        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        if (body.isNotEmpty()) {
                            callback.onSuccess(gson.fromJson(body, typeToken.type))
                        } else {
                            callback.onSuccess()
                        }
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }
    fun getAll(callback: PostCallback<Album>) {
        baseRequest(callback, object : TypeToken<Album>() {}) {
            url(BASE_URL)
            get()
        }
    }
}

interface PostCallback<T> {
    fun onSuccess(result: T) {}
    fun onSuccess() {}
    fun onError(e: Exception) {}
}