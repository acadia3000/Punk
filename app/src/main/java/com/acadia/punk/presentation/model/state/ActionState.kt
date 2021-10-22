package com.acadia.punk.presentation.model.state

sealed class ActionState {
    object None : ActionState()
    data class Load(val withLoadingProgress: Boolean) : ActionState()
    data class LoadMore(val withLoadingProgress: Boolean) : ActionState()
}
