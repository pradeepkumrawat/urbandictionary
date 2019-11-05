package com.sample.dictionary.urbandictionary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.sample.dictionary.urbandictionary.R
import com.sample.dictionary.urbandictionary.model.DictionaryItemModel


//RecyclerViewAdapter class binds the data to the RecyclerView
class RecyclerViewAdapter(mData : List<String>?) : RecyclerView.Adapter<RecyclerViewAdapter.DictionaryViewHolder>() {
    var mData : List<DictionaryItemModel>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DictionaryViewHolder {
        Log.i("DictionaryViewModel","onCreateViewHolder()")
        val view : View = LayoutInflater.from(p0.context).inflate(R.layout.recyclerview_item,p0,false)
        return RecyclerViewAdapter.DictionaryViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.i("DictionaryViewModel","mData?.size = " + mData?.size)
        return  mData?.size ?: 0
    }

    override fun onBindViewHolder(p0: DictionaryViewHolder, p1: Int) {
        Log.i("DictionaryViewModel","onBindViewHolder()")
        p0.txtDefinition.text = mData?.get(p1)?.mDefinition
        p0.txtThumbsUp.text = mData?.get(p1)?.mThumbsUp.toString()
        p0.txtThumbsDown.text = mData?.get(p1)?.mThumbsDown.toString()
    }

    class DictionaryViewHolder(mView : View) : RecyclerView.ViewHolder(mView) {
        var txtDefinition : TextView
        var txtThumbsUp : TextView
        var txtThumbsDown : TextView
        init {
            txtDefinition = mView.findViewById(R.id.txtDefinition)
            txtThumbsUp = mView.findViewById(R.id.txtThumnbsUp)
            txtThumbsDown = mView.findViewById(R.id.txtThumnbsDown)
        }
    }
}