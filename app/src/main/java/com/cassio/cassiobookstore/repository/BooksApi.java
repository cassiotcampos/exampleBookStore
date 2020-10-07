package com.cassio.cassiobookstore.repository;


import com.cassio.cassiobookstore.BuildConfig;
import com.cassio.cassiobookstore.model.Books;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class BooksApi {

  public static final String BASE_URL = "https://www.googleapis.com/books/v1/";

  public static final String FIELDS = "kind,items(volumeInfo/title,volumeInfo/authors,volumeInfo/publisher,volumeInfo/publishedDate,volumeInfo/description,volumeInfo/imageLinks(smallThumbnail)searchInfo(textSnippet),saleInfo/buyLink)";

  public static final Gson GSON = new GsonBuilder()
      .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SS'Z'")
      .create();

  private ApiInterface api;

  private static BooksApi INSTANCE;

  /**
   * Sets up the singleton instance
   *
   * @return Singleton instance
   */
  public static BooksApi getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BooksApi();
    }
    return INSTANCE;
  }

  /**
   * Get list of users in page passed as parameter
   */
  public void getBooks(String q, int maxResults, Integer startIndex, Callback<Books> callback) {
    Call<Books> userResponsePage = api.getBooks(q, maxResults, startIndex, FIELDS);
    userResponsePage.enqueue(callback);
  }

  private BooksApi() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(BuildConfig.DEBUG ?
        HttpLoggingInterceptor.Level.BODY :
        HttpLoggingInterceptor.Level.NONE);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    api = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GSON))
        .client(client)
        .build()
        .create(ApiInterface.class);
  }
}
