package com.application.zaki.githubuser.data.repository

import androidx.paging.PagingData
import com.application.zaki.githubuser.domain.model.ListUsers
import kotlinx.coroutines.flow.Flow

interface IGithubRepository {
    fun getListUsers(): Flow<PagingData<ListUsers>>
}