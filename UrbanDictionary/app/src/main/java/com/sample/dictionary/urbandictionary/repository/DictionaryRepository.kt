package com.sample.dictionary.urbandictionary.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.sample.dictionary.urbandictionary.DictionaryApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit

//DictionaryRepository create the retrofit instance by setting all the necessary details
class DictionaryRepository {
    companion object {
        private val BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com"
        private val CAHCE_SIZE_IN_BYTES : Long = 1024*1024*5 //5MB cache
        private val MAXSTALE = 60 * 60 * 24 * 7 // Offline cache available for 7 days
        private val MAXAGE = 60 // read from cache for 60 seconds even if there is internet connection
        private val HEADER_CACHE_CONTROL = "Cache-Control"
        private val HEADER_PRAGMA = "Pragma"
        private val CACH_NAME = "dictionarycache"
        private val TIMEOUT : Duration = Duration.ofSeconds(10) //10 Sec
        fun getRetrofitInstance() : Retrofit{
            //Creating HttpLoggingInterceptor instance
            val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val cache = Cache(File(DictionaryApplication.Companion.appContext?.cacheDir,CACH_NAME) ,CAHCE_SIZE_IN_BYTES)
            //Creating OkHttpClient instance
            var okHttpClient : OkHttpClient = OkHttpClient().newBuilder()
                //Code to maintain the search results in cache
                .cache(cache)
                //Interceptor the reuqest and read from cache when device is offline
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (!hasNetwork(DictionaryApplication.appContext)!!) {

                        request = request.newBuilder()
                            .header(HEADER_CACHE_CONTROL, "public, only-if-cached, max-stale=$MAXSTALE")
                            .removeHeader(HEADER_PRAGMA)
                            .build()

                    }
                    chain.proceed(request) }
                //Network Interceptor the request data from network or read form cache
                .addNetworkInterceptor { chain ->
                    val response = chain.proceed(chain.request())
                    response.newBuilder()
                        .header(HEADER_CACHE_CONTROL, "public, max-age=$MAXAGE")
                        .removeHeader(HEADER_PRAGMA)
                        .build()}
                //HTTP logging interceptor
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(TIMEOUT)
                .build()

            //Creating and returning retrofit instance
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        //hasNetwork() method check whether internet is available on device or not
        fun hasNetwork(context: Context?): Boolean? {
            var isConnected: Boolean? = false // Initial Value
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }
    }
}