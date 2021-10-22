package com.acadia.punk.presentation.model.mapper

import com.acadia.punk.domain.model.Beer
import com.acadia.punk.domain.model.mapper.Mapper
import com.acadia.punk.presentation.model.BeerListItemModel
import com.acadia.punk.presentation.model.BeerListItemModel.ContentItemModel

class BeerItemModelMapper : Mapper<List<Beer>, List<BeerListItemModel>> {

    override fun mapToModel(from: List<Beer>): List<BeerListItemModel> {
        return from.map { ContentItemModel(it) }
    }
}
