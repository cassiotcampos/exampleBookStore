package com.cassio.cassiobookstore.repository.retrofit.bookapi

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class WebAccess {

    companion object {
        val BASE_URL = BooksApi.BASE_URL
    }

    // Singleton pattern in Kotlin: https://kotlinlang.org/docs/reference/object-declarations.html#object-declarations
    object BookAccess {
        val partsApi : ApiInterface by lazy {
            Log.d("WebAccess", "Creating retrofit client")
            val retrofit = Retrofit.Builder()
                // The 10.0.2.2 address routes request from the Android emulator
                // to the localhost / 127.0.0.1 of the host PC
                .baseUrl(BASE_URL)
                // Moshi maps JSON to classes
                .addConverterFactory(GsonConverterFactory.create())
                // The call adapter handles threads
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            // Create Retrofit client
            return@lazy retrofit.create(ApiInterface::class.java)
        }
    }
}