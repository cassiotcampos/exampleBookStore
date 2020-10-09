package com.cassio.cassiobookstore

import android.app.Application
import android.os.Handler
import com.bumptech.glide.Glide

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Runnable {
                Handler().post(Runnable {
                    Glide.get(this@App).clearMemory()
                    Glide.get(this@App).clearMemory()
                })
            }.run()
        }
    }
}