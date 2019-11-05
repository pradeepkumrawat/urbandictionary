package com.sample.dictionary.urbandictionary.repository

import com.sample.dictionary.urbandictionary.model.DictionaryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DictionaryService {
    //Setting header data for http request
    @Headers("x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com",
        "x-rapidapi-key: 2546aa0c51msh8c902fbb309eef7p161b87jsn9dd23592f0e9")
    @GET("define")
    fun getDefine(@Query("term") query : String) : Call<DictionaryModel>
}