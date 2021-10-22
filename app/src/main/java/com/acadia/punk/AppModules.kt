package com.acadia.punk

import com.acadia.punk.data.network.ApiRemoteDataSource
import com.acadia.punk.data.network.AuthorizationInterceptor
import com.acadia.punk.data.repository.BeerRepository
import com.acadia.punk.domain.model.mapper.BeerMapper
import com.acadia.punk.domain.usecase.BeerUseCase
import com.acadia.punk.presentation.model.mapper.BeerItemModelMapper
import com.acadia.punk.presentation.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

interface AppModules {

    companion object {
        operator fun invoke() = module {
            single { AuthorizationInterceptor() }
            single { ApiRemoteDataSource(authorizationInterceptor = get()) }
            single { BeerRepository(remoteDataSource = get()) }

            single { BeerMapper() }
            single { BeerItemModelMapper() }

            factory { BeerUseCase(repository = get(), mapper = get()) }

            viewModel { MainViewModel(useCase = get(), itemModelMapper = get()) }
        }
    }
}
