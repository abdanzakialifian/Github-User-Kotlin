package com.application.zaki.githubuser.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.application.zaki.githubuser.domain.usecase.IGithubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val githubUseCase: IGithubUseCase) : ViewModel() {
    fun getListUser() = githubUseCase.getListUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = PagingData.empty()
    ).cachedIn(viewModelScope)
}