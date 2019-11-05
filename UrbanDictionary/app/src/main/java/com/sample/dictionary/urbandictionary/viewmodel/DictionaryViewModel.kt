package com.sample.dictionary.urbandictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.dictionary.urbandictionary.model.DictionaryModel
import com.sample.dictionary.urbandictionary.repository.DictionaryRepository
import com.sample.dictionary.urbandictionary.repository.DictionaryService
import retrofit2.Call
import retrofit2.Response

//DictionaryViewModel is a view model which fetches and provide the search data to the UI
class DictionaryViewModel : ViewModel() {
    var mDictionaryService : DictionaryService? = null
    var mData : MutableLiveData<DictionaryModel> = MutableLiveData()
    var mProgress : MutableLiveData<Int> = MutableLiveData()

    fun getResults() : MutableLiveData<DictionaryModel> {
        return mData;
    }

    fun getProgress() : MutableLiveData<Int> {
        return mProgress;
    }
    fun loadResults(searcStr : String) {
        mProgress.value = 1
        if(mDictionaryService == null)
            mDictionaryService = DictionaryRepository.getRetrofitInstance().create(DictionaryService::class.java)

        mDictionaryService?.getDefine(searcStr)?.enqueue(object : retrofit2.Callback<DictionaryModel> {
            override fun onFailure(call: Call<DictionaryModel>, t: Throwable) {
               t.printStackTrace()
               mProgress.value = 0
            }

            override fun onResponse(call: Call<DictionaryModel>, response: Response<DictionaryModel>) {
                mData.value = response.body()
                mProgress.value = 0
            }
        })
    }
}