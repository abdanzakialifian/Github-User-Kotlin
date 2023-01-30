package com.application.zaki.githubuser.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.domain.interfaces.IGithubUseCase
import com.application.zaki.githubuser.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) :
    ViewModel() {
    fun getDetailUser(username: String): StateFlow<UiState<DetailUser>> =
        githubUseCase.getDetailUser(username).stateIn(
            initialValue = UiState.loading(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L)
        )

    fun getFollowersUser(username: String): StateFlow<PagingData<ListUsers>> =
        githubUseCase.getFollowersUser(username)
            .cachedIn(viewModelScope)
            .stateIn(
                initialValue = PagingData.empty(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000L)
            )

    fun getFollowingUser(username: String): StateFlow<PagingData<ListUsers>> =
        githubUseCase.getFollowingUser(username)
            .cachedIn(viewModelScope)
            .stateIn(
                initialValue = PagingData.empty(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000L)
            )

    fun getRepositoriesUser(username: String): Flow<PagingData<RepositoriesUser>> =
        githubUseCase.getRepositoriesUser(username)
            .cachedIn(viewModelScope)
            .stateIn(
                initialValue = PagingData.empty(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000L)
            )
}