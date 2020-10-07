package com.cassio.cassiobookstore.mock



import com.cassio.cassiobookstore.repository.ApiInterface
import com.cassio.cassiobookstore.repository.BooksApi
import net.vidageek.mirror.dsl.Mirror
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * (by Cassio Ribeiro)
 * Chamadas Mock atraves do MockWebServer e Mirror
 * (Mirror utiliza REFLECTION, por isso Retrofit esta em JAVA!!!)
 * --------------------------
 */
lateinit var server: MockWebServer

fun mockApiCallBefore() {
    server = MockWebServer()
    server.start(8080)
    setupServerUrl()
}

@Throws(IOException::class)
fun mockApiCallAfter() {
    server.shutdown()
}

/**
 * The next X requests will be mocked with mockData instead call API
 */
fun mockApiCallEnqueue(mockData : String, x : Int){
    for(i in 0..x)
        server.enqueue(MockResponse().setResponseCode(200).setBody(mockData))
}

private fun setupServerUrl() {

    // original api to be replaced
    val booksApi: BooksApi = BooksApi.getInstance()

    // mockedApi
    val url = server.url("/").toString()
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val mockedApi = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(BooksApi.GSON))
        .client(client)
        .build()
        .create(ApiInterface::class.java)


    // replacement
    Mirror()
        .on(booksApi)
        .set()
        .field("api") // Reflection trick (only for tests)
        .withValue(mockedApi)
}