package com.sample.dictionary.urbandictionary.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.sample.dictionary.urbandictionary.DictionaryApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

//DictionaryRepository create the retrofit instance by setting all the necessary details
class DictionaryRepository {
    companion object {
        private val BASE_URL : String = "https://mashape-community-urban-dictionary.p.rapidapi.com"
        private val CAHCE_SIZE_IN_BYTES : Long = 1024*1024*5 //5MB cache
        private val TIMEOUT : Duration = Duration.ofSeconds(10) //10 Sec
        fun getRetrofitInstance() : Retrofit{
            //Creating HttpLoggingInterceptor instance
            val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            //Creating OkHttpClient instance
            var okHttpClient : OkHttpClient = OkHttpClient().newBuilder()
                .cache(DictionaryApplication.Companion.appContext?.cacheDir?.let { Cache(it, CAHCE_SIZE_IN_BYTES) })
                //Code to maintain the search results in cache
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(DictionaryApplication.appContext)!!)
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                    chain.proceed(request) }
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