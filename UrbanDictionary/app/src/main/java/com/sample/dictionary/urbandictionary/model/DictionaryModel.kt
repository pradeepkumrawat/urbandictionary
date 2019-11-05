package com.sample.dictionary.urbandictionary.model

import com.google.gson.annotations.SerializedName

class DictionaryModel {
    @SerializedName("list")
    var mDictionaryItems : List<DictionaryItemModel> = mutableListOf()
}