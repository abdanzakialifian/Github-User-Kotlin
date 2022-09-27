package com.application.zaki.githubuser.domain.usecase

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.ListUsers
import kotlinx.coroutines.flow.Flow

interface IGithubUseCase {
    fun getListUsers(): Flow<PagingData<ListUsers>>
}