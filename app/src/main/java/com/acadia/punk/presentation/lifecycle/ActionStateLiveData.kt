package com.acadia.punk.presentation.lifecycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.acadia.punk.presentation.model.state.ActionState
import com.acadia.punk.presentation.model.state.StateResult
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class ActionStateLiveData<T>(
    initialLastUpdatedAt: Long = 0,
    fetch: (suspend ActionStateLiveData<T>.() -> T)
) : MutableLiveData<StateResult<T>>() {

    constructor(
        initialLastUpdatedAt: Long = 0,
        fetch: (suspend ActionStateLiveData<T>.() -> T),
        fetchMore: (suspend ActionStateLiveData<T>.() -> T)
    ) : this(initialLastUpdatedAt, fetch) {
        this.fetchMore = fetchMore
    }

    private val action = MutableLiveData<ActionState>(ActionState.None)

    private lateinit var liveDataScope: CoroutineScope
    private var job: Job? = null

    private val observer = Observer<ActionState> {
        when (it) {
            is ActionState.Load -> {
                if (it.withLoadingProgress) {
                    emit(StateResult.Loading)
                }

                job = liveDataScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            fetch()
                        }
                        emit(StateResult.Success(response))
                    } catch (ignored: CancellationException) {
                    } catch (e: Throwable) {
                        emit(StateResult.Failure(e))
                    }
                }
            }
            is ActionState.LoadMore -> {
                if (it.withLoadingProgress) {
                    emit(StateResult.Loading)
                }

                fetchMore?.let { fetchMore ->
                    job = liveDataScope.launch {
                        try {
                            val response = withContext(Dispatchers.IO) {
                                fetchMore()
                            }
                            emit(StateResult.Success(response))
                        } catch (ignored: CancellationException) {
                        } catch (e: Throwable) {
                            emit(StateResult.Failure(e))
                        }
                    }
                }
            }
        }
    }

    private var lastUpdatedAt: Long = initialLastUpdatedAt
    private var fetchMore: (suspend ActionStateLiveData<T>.() -> T)? = null

    fun emit(StateResult: StateResult<T>) {
        value = StateResult

        if (StateResult !is StateResult.Loading) {
            action.value = ActionState.None
        }

        if (StateResult is StateResult.Success<T>) {
            lastUpdatedAt = Calendar.getInstance().timeInMillis
        }
    }

    override fun onActive() {
        super.onActive()

        liveDataScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        action.observeForever(observer)
    }

    override fun onInactive() {
        super.onInactive()

        action.removeObserver(observer)
        liveDataScope.cancel()
    }

    fun isLoading() = (action.value is ActionState.Load || action.value is ActionState.LoadMore)

    fun load(withLoadingProgress: Boolean = true): Boolean {
        if (isLoading()) {
            return false
        }

        action.value = ActionState.Load(withLoadingProgress)
        return true
    }

    fun loadMore(withLoadingProgress: Boolean = false): Boolean {
        if (isLoading()) {
            return false
        }

        action.value = ActionState.LoadMore(withLoadingProgress)
        return true
    }

    fun refresh(intervalInSeconds: Long): Boolean {
        if (intervalInSeconds <= 1) {
            return false
        }

        val diff = Calendar.getInstance().timeInMillis - lastUpdatedAt
        val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff)

        return if (diffInSeconds >= intervalInSeconds) {
            load(false)
        } else {
            false
        }
    }

    fun cancel() {
        job?.cancel()
        action.value = ActionState.None
    }
}
