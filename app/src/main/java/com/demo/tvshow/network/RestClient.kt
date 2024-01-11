package com.demo.tvshow.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.demo.tvshow.BuildConfig
import com.demo.tvshow.K_BEARER
import com.demo.tvshow.TVShowApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RestClient {
    private val cache: Cache
    private lateinit var gson: Gson
    private lateinit var httpClient: OkHttpClient
    lateinit var retrofit: Retrofit
    var SIZE_OF_CACHE = (5 * 1024 * 1024 // 5 MB
            ).toLong()

    init {
        cache = Cache(TVShowApplication.mApplicationContext!!.cacheDir, SIZE_OF_CACHE)
        initHttpClient()
        initGson()
        initRetrofit()
    }

    private fun initHttpClient() {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        val networkInterceptor = Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header(
                    "client-id",
                    "tvshow-app-android"
                ).header(
                    "Authorization",
                    "$K_BEARER ${LocalAppMemCache.token}"
                )
                .method(original.method, original.body)
            if (hasNetwork()) {
                /*
                 *  If there is Internet, get the cache that was stored 5 seconds ago.
                 *  If the cache is older than 5 seconds, then discard it,
                 *  and indicate an error in fetching the response.
                 *  The 'max-age' attribute is responsible for this behavior.
                 */
                requestBuilder.header("Cache-Control", "public, max-age=" + 5)
            } else {
                /*
                 *  If there is no Internet, get the cache that was stored 7 days ago.
                 *  If the cache is older than 7 days, then discard it,
                 *  and indicate an error in fetching the response.
                 *  The 'max-stale' attribute is responsible for this behavior.
                 *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                 */
                requestBuilder.header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                )
            }
            val request: Request = requestBuilder.build()
            Log.d("Header", request.toString())
            val response = chain.proceed(request)
            if (response.code == 504 || response.code == 503) {
                throw IOException("Server is unreachable")
            }
            response
        }
        httpClient = OkHttpClient.Builder()
            .connectTimeout(240, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS)
            .writeTimeout(240, TimeUnit.SECONDS)
            .callTimeout(240, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(networkInterceptor)
            .cache(cache)
            .build()
    }

    private fun initGson() {
        gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
    }

    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    fun <S> create(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    private fun hasNetwork(): Boolean {
        return try {
            val connectivityManager =
                TVShowApplication.mApplicationContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            true
        }
    }

}