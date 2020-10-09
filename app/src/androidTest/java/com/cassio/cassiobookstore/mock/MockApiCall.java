package com.cassio.cassiobookstore.mock;

import com.cassio.cassiobookstore.repository.ApiInterface;
import com.cassio.cassiobookstore.repository.BooksApi;

import net.vidageek.mirror.dsl.Mirror;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MockApiCall {

    private static MockWebServer server;

    public static void mockApiCallBefore() throws IOException {
        server = new MockWebServer();
        server.start(8080);
        setupServerUrl();
    }


    public static void mockApiCallAfter()  {
        try {
            server.shutdown();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * The next X requests will be mocked with mockData instead call API
     */
    public static void mockApiCallEnqueue(String mockData, int x){
        for(int i = 0; i < x; i++)
            server.enqueue(new MockResponse().setResponseCode(200).setBody(mockData));
    }

    private static void setupServerUrl() {

        // original api to be replaced
        BooksApi booksApi = BooksApi.getInstance();
        String url = server.url("/").toString();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Object mockedApi = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(BooksApi.GSON))
                .client(client)
                .build()
                .create(ApiInterface.class);


        // replacement
        new Mirror()
                .on(booksApi)
                .set()
                .field("api") // Reflection trick (only for tests)
                .withValue(mockedApi);
    }

}
