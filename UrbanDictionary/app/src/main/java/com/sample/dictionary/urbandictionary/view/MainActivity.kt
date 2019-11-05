package com.sample.dictionary.urbandictionary.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.dictionary.urbandictionary.R
import com.sample.dictionary.urbandictionary.viewmodel.DictionaryViewModel
import com.sample.dictionary.urbandictionary.adapter.RecyclerViewAdapter
import com.sample.dictionary.urbandictionary.model.DictionaryItemModel
import com.sample.dictionary.urbandictionary.model.DictionaryModel

class MainActivity : AppCompatActivity() {
    lateinit var mDictionaryViewModel : DictionaryViewModel
    lateinit var mRecyclerViewAdapter : RecyclerViewAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRecyclerView : RecyclerView
    lateinit var mTxtError : TextView
    lateinit var mProgressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get DictionaryViewModel and observe on DictionaryModel data for change
        mDictionaryViewModel = ViewModelProviders.of(this).get(DictionaryViewModel::class.java)
        mDictionaryViewModel.getResults().observe(this, Observer {
            Log.i(MainActivity::class.java.simpleName,"onChange()")
            //If results are empty then show error UI else show the actual search results.
            if(it != null) {
                if (it.mDictionaryItems?.isEmpty()) {
                    mRecyclerView.visibility = View.GONE
                    mTxtError.visibility = View.VISIBLE
                } else {
                    mRecyclerView.visibility = View.VISIBLE
                    mTxtError.visibility = View.GONE
                    mRecyclerViewAdapter.mData = it.mDictionaryItems
                    mRecyclerViewAdapter.notifyDataSetChanged()
                }
            } else {
                mRecyclerView.visibility = View.GONE
                mTxtError.visibility = View.VISIBLE
            }
        })

        //Observe on progress bar data
        mDictionaryViewModel.getProgress().observe(this, Observer {
            if (it == 1) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        //Create layout manager and adpater object and set them for RecylerView
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerViewAdapter = RecyclerViewAdapter(listOf())
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.adapter = mRecyclerViewAdapter
        mRecyclerView.layoutManager = mLayoutManager

        mProgressBar = findViewById(R.id.progressBar)
        mTxtError = findViewById(R.id.txtError)
    }

    /**
     * onSearchButtonClick() is used to search results based on search string.
     */
    fun onSearchButtonClick(view : View) {
        Log.i(MainActivity::class.java.simpleName,"onButtonClick()")
        val searchStr : String = findViewById<EditText>(R.id.etSearch).text.toString()
        mDictionaryViewModel.loadResults(searchStr)
        hideKeyPad(view)
    }

    fun hideKeyPad(view : View) {
        val inputMethodManager : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
            InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    //Show progress bar on loadResults request
    fun showProgress() {
        mProgressBar.visibility = View.VISIBLE
        mRecyclerView.visibility = View.GONE
        mTxtError.visibility = View.GONE
    }

    //Hide progress bar when done with searching
    fun hideProgress(){
        mProgressBar.visibility = View.GONE
    }

    //When user clicks on thumbs up icon on UI sort the results on based on Thumbs ups and update the UI
    fun onThumbsUp(view : View) {
        val dictionaryModel : DictionaryModel = mDictionaryViewModel.getResults().value as DictionaryModel
        dictionaryModel.mDictionaryItems = dictionaryModel.mDictionaryItems.sortedWith(Comparator<DictionaryItemModel> { o1, o2 ->
            when {
                o1.mThumbsUp > o2.mThumbsUp -> -1
                o1.mThumbsUp < o2.mThumbsUp -> 1
                else -> 0}
        })
        mDictionaryViewModel.getResults().value = dictionaryModel
    }

    //When user clicks on thumbs down icon on UI sort the results on based on Thumbs downs and update the UI
    fun onThumbsDown(view : View) {
        val dictionaryModel : DictionaryModel = mDictionaryViewModel.getResults().value as DictionaryModel
        dictionaryModel.mDictionaryItems = dictionaryModel.mDictionaryItems.sortedWith(Comparator<DictionaryItemModel> { o1, o2 ->
            when {
                o1.mThumbsDown > o2.mThumbsDown -> -1
                o1.mThumbsDown < o2.mThumbsDown -> 1
                else -> 0}
        })
        mDictionaryViewModel.getResults().value = dictionaryModel
    }
}
