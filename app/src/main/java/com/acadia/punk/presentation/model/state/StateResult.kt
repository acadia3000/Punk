package com.acadia.punk.presentation.model.state

sealed class StateResult<out R> {
    object Loading : StateResult<Nothing>()
    data class Success<out T>(val item: T) : StateResult<T>()
    data class Failure(val cause: Throwable) : StateResult<Nothing>()
}
