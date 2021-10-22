package com.acadia.punk.data.repository

import com.acadia.punk.data.network.ApiRemoteDataSource

class BeerRepository(private val remoteDataSource: ApiRemoteDataSource) {

    suspend fun list(query: String, page: Int, perPage: Int = 25) =
        remoteDataSource.beers(query, page, perPage)

    suspend fun detail(id: Int) = remoteDataSource.beerDetail(id)
}
