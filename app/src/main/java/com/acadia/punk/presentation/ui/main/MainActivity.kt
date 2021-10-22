package com.acadia.punk.presentation.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acadia.punk.R
import com.acadia.punk.databinding.ActivityMainBinding
import com.acadia.punk.presentation.binding.SimpleDataBindingPresenter
import com.acadia.punk.presentation.model.BeerListItemModel
import com.acadia.punk.presentation.model.BeerListItemModel.ContentItemModel
import com.acadia.punk.presentation.model.state.StateResult
import com.acadia.punk.presentation.ui.detail.DetailActivity
import com.acadia.punk.util.hideKeyboard
import com.acadia.punk.util.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainListAdapter: MainListAdapter

    private var firstLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        ).apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel
        }.also {
            binding = it
        }

        initViews()
        initObserves()

        binding.searchView.setQuery("IPA", true)
    }

    private fun initViews() {
        mainListAdapter = MainListAdapter(object : SimpleDataBindingPresenter() {
            override fun onClick(view: View, item: Any) {
                when (item) {
                    is ContentItemModel -> {
                        binding.searchView.clearFocus()
                        startActivity<DetailActivity> {
                            putExtra(DetailActivity.EXTRA_BEER, item.beer)
                        }
                    }
                }
            }
        })

        binding.rvBeers.apply {
            adapter = mainListAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(context)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    (layoutManager as? LinearLayoutManager)?.run {
                        mainViewModel.fetchMore(findLastVisibleItemPosition())
                    }
                }
            })
        }

        binding.refresher.apply {
            setColorSchemeResources(R.color.purple_500)
            setOnRefreshListener {
                mainViewModel.fetch(true).also {
                    if (!it) isRefreshing = false
                    firstLoading = true
                }
            }
        }

        binding.searchView.apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextChange(text: String?): Boolean {
                    mainViewModel.query.value = text
                    return true
                }

                override fun onQueryTextSubmit(text: String?): Boolean {
                    mainViewModel.fetch()
                    hideKeyboard()
                    return true
                }
            })
        }
    }

    private fun initObserves() {
        mainViewModel.beers.observe(this, { data ->
            when (data) {
                is StateResult.Loading -> {
                    firstLoading = true
                }
                is StateResult.Success<List<BeerListItemModel>> -> {
                    mainListAdapter.submitList(data.item) {
                        if (firstLoading) {
                            binding.rvBeers.scrollToPosition(0)
                            firstLoading = false
                        }
                    }

                    binding.refresher.isRefreshing = false
                }
                is StateResult.Failure -> {
                    mainListAdapter.submitList(null)
                    binding.refresher.isRefreshing = false

                    Toast.makeText(
                        applicationContext,
                        data.cause.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                }
            }
        })
    }
}
