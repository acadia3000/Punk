package com.acadia.punk.data.network

import com.acadia.punk.domain.model.Beer
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@JvmSuppressWildcards
interface ApiRemoteDataSource {

    @GET("beers")
    suspend fun beers(
        @Query("beer_name") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<Beer>>

    @GET("beers/random")
    suspend fun randomBeer(): Response<Beer>

    @GET("beers/{id}")
    suspend fun beerDetail(@Path("id") id: Int): Response<Beer>

    companion object {
        private const val BASE_URL = "https://api.punkapi.com/v2/"

        operator fun invoke(): ApiRemoteDataSource {
            val okHttpBuilder = OkHttpClient.Builder()

            return Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setFieldNamingPolicy(
                            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
                        ).create()
                    )
                )
                .build()
                .create(ApiRemoteDataSource::class.java)
        }
    }
}
