package com.sample.dictionary.urbandictionary.model

import com.google.gson.annotations.SerializedName

//DictionaryItemModel is a data model corresponds to resulted data array
class DictionaryItemModel {
    @SerializedName("definition")
    lateinit var mDefinition : String

    @SerializedName("permalink")
    lateinit var mPermalink : String

    @SerializedName("thumbs_up")
    var mThumbsUp : Int = 0

    @SerializedName("author")
    lateinit var mAuthor : String

    @SerializedName("word")
    lateinit var mWord : String

    @SerializedName("defid")
    var mDefid : Int = 0

    @SerializedName("current_vote")
    lateinit var mCurrentVote : String

    @SerializedName("written_on")
    lateinit var mWrittenOn : String

    @SerializedName("example")
    lateinit var mExample : String

    @SerializedName("thumbs_down")
    var mThumbsDown : Int = 0
}