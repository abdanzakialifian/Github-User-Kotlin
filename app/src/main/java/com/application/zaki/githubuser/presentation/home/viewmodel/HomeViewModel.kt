package com.application.zaki.githubuser.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import com.application.zaki.githubuser.utils.NetworkResult
import com.application.zaki.githubuser.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) : ViewModel() {
    private val _listUsers = MutableStateFlow<Resource<PagingData<ListUsers>>>(Resource.loading())
    val listUsers: StateFlow<Resource<PagingData<ListUsers>>> = _listUsers

    init {
        getListUsers()
    }

    private fun getListUsers() {
        NetworkResult.Loading(null)
        viewModelScope.launch {
            githubUseCase.getListUsers()
                .cachedIn(viewModelScope)
                .catch { e ->
                    _listUsers.value = Resource.error(e.message.toString())
                }
                .collect {
                    _listUsers.value = Resource.success(it)
                }
        }
    }

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