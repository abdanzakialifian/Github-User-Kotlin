package com.application.zaki.githubuser.presentation.home.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.githubuser.domain.interfaces.IGithubUseCase
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.utils.getQueryTextChangeStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) : ViewModel() {
    val getListUsers: StateFlow<PagingData<ListUsers>> = githubUseCase.getListUsers()
        .cachedIn(viewModelScope)
        .stateIn(
            initialValue = PagingData.empty(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L)
        )

    fun searchUser(query: String): StateFlow<PagingData<ListUsers>> =
        githubUseCase.getUsers(query)
            .cachedIn(viewModelScope)
            .stateIn(
                initialValue = PagingData.empty(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000L)
            )

    fun addUser(user: User) = githubUseCase.addUser(user)

    fun deleteUser(user: User) = githubUseCase.deleteUser(user)

    fun getUserById(id: Int): Boolean = githubUseCase.getUserById(id)

    @OptIn(FlowPreview::class)
    fun searchFlow(searchView: SearchView): Flow<String> =
        searchView.getQueryTextChangeStateFlow()
            .debounce(600L)
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
}