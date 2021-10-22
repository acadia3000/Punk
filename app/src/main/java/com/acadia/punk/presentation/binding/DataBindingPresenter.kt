package com.acadia.punk.presentation.binding

import android.view.View

interface DataBindingPresenter {
    fun onClick(view: View, item: Any)
    fun onLongClick(view: View, item: Any): Boolean
    fun onCheckedChanged(view: View, item: Any, checked: Boolean)
}
