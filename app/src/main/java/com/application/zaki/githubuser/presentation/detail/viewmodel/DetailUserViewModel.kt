package com.application.zaki.githubuser.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import com.application.zaki.githubuser.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) :
    ViewModel() {
    private val _detailUser = MutableStateFlow<Resource<DetailUser>>(Resource.loading())
    val detailUser: StateFlow<Resource<DetailUser>> = _detailUser

    private val _listFollowers =
        MutableStateFlow<Resource<PagingData<ListUsers>>>(Resource.loading())
    val listFollowers: StateFlow<Resource<PagingData<ListUsers>>> = _listFollowers

    private val _listFollowing =
        MutableStateFlow<Resource<PagingData<ListUsers>>>(Resource.loading())
    val listFollowing: StateFlow<Resource<PagingData<ListUsers>>> = _listFollowing

    private val _listRepositories =
        MutableStateFlow<Resource<PagingData<RepositoriesUser>>>(Resource.loading())
    val listRepositories: StateFlow<Resource<PagingData<RepositoriesUser>>> = _listRepositories

    fun getDetailUser(username: String) {
        _detailUser.value = Resource.loading()
        viewModelScope.launch {
            githubUseCase.getDetailUser(username)
                .catch { e ->
                    _detailUser.value = Resource.error(e.message.toString())
                }
                .collect {
                    _detailUser.value = Resource.success(it)
                }
        }
    }

    fun getFollowersUser(username: String) {
        _listFollowers.value = Resource.loading()
        viewModelScope.launch {
            githubUseCase.getFollowersUser(username)
                .catch { e ->
                    _listFollowers.value = Resource.error(e.message.toString())
                }
                .collect {
                    _listFollowers.value = Resource.success(it)
                }
        }
    }

    fun getFollowingUser(username: String) {
        _listFollowing.value = Resource.loading()
        viewModelScope.launch {
            githubUseCase.getFollowingUser(username)
                .catch { e ->
                    _listFollowing.value = Resource.error(e.message.toString())
                }
                .collect {
                    _listFollowing.value = Resource.success(it)
                }
        }
    }

    fun getRepositoriesUser(username: String) {
        _listRepositories.value = Resource.loading()
        viewModelScope.launch {
            githubUseCase.getRepositoriesUser(username)
                .catch { e ->
                    _listRepositories.value = Resource.error(e.message.toString())
                }
                .collect {
                    _listRepositories.value = Resource.success(it)
                }
        }
    }
}