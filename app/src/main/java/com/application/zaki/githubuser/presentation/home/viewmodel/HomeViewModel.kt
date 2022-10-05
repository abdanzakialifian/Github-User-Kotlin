package com.application.zaki.githubuser.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import com.application.zaki.githubuser.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) : ViewModel() {
    fun getListUser() = githubUseCase.getListUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NetworkResult.Loading(null)
    )

    // search user with kotlin flow
    val querySearch = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResult = querySearch.debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest { query ->
            githubUseCase.getUsers(query).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = NetworkResult.Loading(null)
            )
        }
}