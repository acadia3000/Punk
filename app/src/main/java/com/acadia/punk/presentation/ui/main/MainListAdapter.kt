package com.acadia.punk.presentation.ui.main

import androidx.databinding.ViewDataBinding
import com.acadia.punk.presentation.binding.DataBindingAdapter
import com.acadia.punk.presentation.binding.DataBindingPresenter
import com.acadia.punk.presentation.binding.SimpleDataBindingViewHolder
import com.acadia.punk.presentation.binding.SimpleItemDiffCallback
import com.acadia.punk.presentation.model.BeerListItemModel

class MainListAdapter(presenter: DataBindingPresenter? = null) :
    DataBindingAdapter<BeerListItemModel>(presenter, SimpleItemDiffCallback()) {

    override fun getItemViewType(position: Int) = getItem(position).layoutResId
    override fun createDataBindingViewHolder(binding: ViewDataBinding) =
        SimpleDataBindingViewHolder<BeerListItemModel>(binding)
}
