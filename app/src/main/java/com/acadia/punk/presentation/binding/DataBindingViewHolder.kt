package com.acadia.punk.presentation.binding

import androidx.databinding.library.baseAdapters.BR
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingViewHolder<T>(internal val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, presenter: DataBindingPresenter?) {
        presenter?.let {
            binding.setVariable(BR.presenter, it)
        }
        bindItem(item)
        binding.executePendingBindings()
    }

    abstract fun bindItem(item: T)
}
