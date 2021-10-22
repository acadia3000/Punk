package com.acadia.punk.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.acadia.punk.domain.usecase.BeerUseCase
import com.acadia.punk.presentation.lifecycle.ActionStateLiveData
import com.acadia.punk.presentation.model.BeerListItemModel
import com.acadia.punk.presentation.model.BeerListItemModel.FooterLoadMoreItemModel
import com.acadia.punk.presentation.model.mapper.BeerItemModelMapper
import com.acadia.punk.presentation.model.state.StateResult
import com.acadia.punk.presentation.model.state.StateResult.*
import com.acadia.punk.util.removeIsInstance

class MainViewModel(
    private val useCase: BeerUseCase,
    itemModelMapper: BeerItemModelMapper
) : ViewModel() {

    companion object {
        private const val FETCH_MORE_VISIBLE_THRESHOLD = 5
    }

    private val mutableBeers = ActionStateLiveData<List<BeerListItemModel>>(fetch = {
        runCatching {
            useCase.list(query.value ?: "", page)
        }.mapCatching {
            itemModelMapper.mapToModel(it)
        }.getOrThrow()
    }, fetchMore = {
        val itemModels =
            (value as? Success<List<BeerListItemModel>>)?.item?.toMutableList()?.apply {
                removeIsInstance<FooterLoadMoreItemModel>()
            } ?: emptyList()

        val newItems = runCatching {
            useCase.list(query.value ?: "", page)
        }.mapCatching {
            itemModelMapper.mapToModel(it)
        }.onSuccess {
            endOfPage = it.isEmpty()
            ++page
        }.getOrNull() ?: emptyList()

        itemModels + newItems
    })

    val beers: LiveData<StateResult<List<BeerListItemModel>>> = mutableBeers

    val query = MutableLiveData<String>()

    val isLoading = Transformations.map(beers) { state ->
        state is Loading
    }

    val isEmpty = Transformations.map(beers) { state ->
        state is Success && state.item.isNullOrEmpty()
    }

    val hasError = Transformations.map(beers) { state ->
        state is Failure
    }

    private var page = 1
    private var endOfPage = false

    fun fetch(pullToRefresh: Boolean = false): Boolean {
        if (query.value.isNullOrEmpty()) {
            return false
        }

        page = 1
        endOfPage = false
        return mutableBeers.load(pullToRefresh.not())
    }

    fun fetchMore(visiblePosition: Int) {
        if (endOfPage || mutableBeers.isLoading() || query.value.isNullOrEmpty()) {
            return
        }

        val fetched = (beers.value as? Success<List<BeerListItemModel>>)?.item ?: return
        if (fetched.size > FETCH_MORE_VISIBLE_THRESHOLD && fetched.size <= visiblePosition + FETCH_MORE_VISIBLE_THRESHOLD) {
            mutableBeers.apply {
                value = Success(
                    fetched.toMutableList().apply {
                        add(FooterLoadMoreItemModel())
                    }
                )
            }
            mutableBeers.loadMore()
        }
    }
}
