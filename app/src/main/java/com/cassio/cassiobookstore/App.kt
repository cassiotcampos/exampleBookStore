package com.cassio.cassiobookstore

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import com.bumptech.glide.Glide
import com.cassio.cassiobookstore.di.booksApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(booksApiModule)
        }

        // For purpose of better img tests
        Glide.get(this@App).clearMemory()
        ClearGlide(this).execute()
    }

    class ClearGlide(private val mContext: Context) : AsyncTask<Void, Void, String>() {
        @Override
        override fun doInBackground(vararg params: Void?): String? {
            Glide.get(mContext).clearDiskCache()
            return null
        }
    }
}