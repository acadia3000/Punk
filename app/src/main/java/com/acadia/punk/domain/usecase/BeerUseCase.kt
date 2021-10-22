package com.acadia.punk.domain.usecase

import com.acadia.punk.data.repository.BeerRepository
import com.acadia.punk.domain.model.mapper.BeerMapper

class BeerUseCase(private val repository: BeerRepository, private val mapper: BeerMapper) {

    suspend fun list(query: String, page: Int) = runCatching {
        repository.list(query, page)
    }.mapCatching { response ->
        mapper.mapToModel(response)
    }.onFailure {
        // handleError(throwable)
    }.getOrThrow()
}
