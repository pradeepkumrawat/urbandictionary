package com.sample.dictionary.urbandictionary

import android.app.Application
import android.content.Context

class DictionaryApplication : Application() {
    companion object {
        public var appContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext;
    }
}