package com.application.zaki.githubuser.presentation.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.zaki.githubuser.domain.model.User
import com.application.zaki.githubuser.domain.usecase.GithubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val githubUseCase: GithubUseCase) :
    ViewModel() {

    val getAllUser: StateFlow<List<User>> = githubUseCase.getAllUser().stateIn(
        initialValue = listOf(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000L)
    )

    fun deleteUser(user: User) = githubUseCase.deleteUser(user)
}