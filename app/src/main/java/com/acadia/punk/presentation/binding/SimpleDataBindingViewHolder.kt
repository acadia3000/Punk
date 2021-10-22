package com.acadia.punk.presentation.binding

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR

class SimpleDataBindingViewHolder<T>(binding: ViewDataBinding) :
    DataBindingViewHolder<T>(binding) {

    override fun bindItem(item: T) {
        binding.setVariable(BR.item, item)
    }
}
