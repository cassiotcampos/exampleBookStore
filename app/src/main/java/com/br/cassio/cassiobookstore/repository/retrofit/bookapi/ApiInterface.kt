package com.br.cassio.cassiobookstore.repository.retrofit.bookapi

import com.br.cassio.cassiobookstore.model.generated.java.Books
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("volumes")
    fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("startInd") startIndex: Int?

    ): Call<Books>

}
