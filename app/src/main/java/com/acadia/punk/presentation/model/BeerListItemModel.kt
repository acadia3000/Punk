package com.acadia.punk.presentation.model

import androidx.annotation.LayoutRes
import com.acadia.punk.R
import com.acadia.punk.domain.model.Beer
import com.acadia.punk.presentation.binding.SimpleItemDiffCallback.DiffCallback

sealed class BeerListItemModel(@LayoutRes val layoutResId: Int) :
    DiffCallback {

    data class ContentItemModel(val beer: Beer) :
        BeerListItemModel(R.layout.beer_content_list_item)

    class FooterLoadMoreItemModel :
        BeerListItemModel(R.layout.loading_list_item)

    override fun areItemsTheSame(other: DiffCallback): Boolean {
        return if (this is ContentItemModel && other is ContentItemModel) {
            this.beer == other.beer
        } else {
            super.areItemsTheSame(other)
        }
    }
}
