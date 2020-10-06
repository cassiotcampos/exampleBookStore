package com.cassio.cassiobookstore.repository.retrofit.bookapi;

import com.cassio.cassiobookstore.model.generated.java.Books;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

  @GET("/")
  Call<Books> getBooks(@Query("page") int page, @Query("results") int results);
}
