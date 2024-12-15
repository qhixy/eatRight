package mk14.first.eatright.retrofit

import mk14.first.eatright.api.ApiService
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://eatright-api-662838524736.asia-southeast2.run.app/"

    // Persist cookies across requests
    private val cookieJar = object : CookieJar {
        private val cookies = mutableListOf<Cookie>()

        // Save cookies from the response
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies.addAll(cookies) // Add all cookies from the response
        }

        // Load cookies for the request (use the stored cookies)
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies // Return stored cookies for requests
        }
    }

    // OkHttpClient with timeout settings
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .connectTimeout(15, TimeUnit.SECONDS)  // Increase connection timeout
        .readTimeout(15, TimeUnit.SECONDS)     // Increase read timeout
        .build()

    // Retrofit client initialization with Gson converter and OkHttp client
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for API requests
            .client(okHttpClient) // Set the OkHttpClient with cookie jar and timeouts
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
            .build()
            .create(ApiService::class.java) // Create the ApiService interface
    }
}