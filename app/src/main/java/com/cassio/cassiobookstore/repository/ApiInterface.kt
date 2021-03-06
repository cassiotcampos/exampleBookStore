package com.cassio.cassiobookstore.repository

import com.cassio.cassiobookstore.model.Books
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("volumes")
    fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("startIndex") startIndex: Int,
        @Query("fields") fields : String

    ): Call<Books>

}
