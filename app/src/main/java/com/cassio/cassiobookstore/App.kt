package com.cassio.cassiobookstore

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import com.bumptech.glide.Glide

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // For purpose of better img tests
        Glide.get(this@App).clearMemory()
        clearGlide(this).execute()
    }

    class clearGlide(val mContext: Context) : AsyncTask<Void, Void, String>() {
        @Override
        override fun doInBackground(vararg params: Void?): String? {
            Glide.get(mContext).clearDiskCache()
            return null
        }
    }
}