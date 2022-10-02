package com.application.zaki.githubuser.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import com.application.zaki.githubuser.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailUserViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) :
    ViewModel() {
    fun getDetailUser(username: String) = githubUseCase.getDetailUser(username).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NetworkResult.Loading(null)
    )
}