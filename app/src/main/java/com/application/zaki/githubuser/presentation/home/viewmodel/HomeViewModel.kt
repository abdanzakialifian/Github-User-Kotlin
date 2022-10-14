package com.application.zaki.githubuser.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import com.application.zaki.githubuser.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) : ViewModel() {
    private val _listUsers = MutableStateFlow<Resource<PagingData<ListUsers>>>(Resource.loading())
    val listUsers: StateFlow<Resource<PagingData<ListUsers>>> = _listUsers

    private val _users = MutableStateFlow<Resource<PagingData<ListUsers>>>(Resource.loading())
    val users: StateFlow<Resource<PagingData<ListUsers>>> = _users

    init {
        getListUsers()
    }

    private fun getListUsers() {
        _listUsers.value = Resource.loading()
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

    fun searchUser(query: String) {
        _users.value = Resource.loading()
        viewModelScope.launch {
            githubUseCase.getUsers(query)
                .cachedIn(viewModelScope)
                .catch { e ->
                    _users.value = Resource.error(e.message.toString())
                }
                .collect {
                    _users.value = Resource.success(it)
                }
        }
    }
}