package ru.netology.singlealbum.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.singlealbum.dto.Album
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepository @Inject constructor() {

    @Inject
    lateinit var client: OkHttpClient

    private val gson = Gson()

    companion object {
        private const val BASE_URL = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
        private val jsonType = "application/json".toMediaType()
    }
    private fun <T> baseRequest(
        callback: AlbumCallback<T>,
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
    fun getAlbum(callback: AlbumCallback<Album>) {
        baseRequest(callback, object : TypeToken<Album>() {}) {
            url(BASE_URL)
            get()
        }
    }
}

interface AlbumCallback<T> {
    fun onSuccess(result: T) {}
    fun onSuccess() {}
    fun onError(e: Exception) {}
}