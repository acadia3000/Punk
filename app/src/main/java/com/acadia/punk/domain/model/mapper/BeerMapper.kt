package com.acadia.punk.domain.model.mapper

import com.acadia.punk.domain.model.Beer
import retrofit2.Response

class BeerMapper : Mapper<Response<List<Beer>>?, List<Beer>> {

    override fun mapToModel(from: Response<List<Beer>>?) =
        from?.body() ?: throw RuntimeException(from?.errorBody()?.string())
}
