package com.application.zaki.githubuser.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.zaki.githubuser.data.source.remote.ApiService
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.utils.DataMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersPagingSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, ListUsers>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListUsers> {
        return try {
            val page = params.key ?: 0
            val response = apiService.getListUser(page, params.loadSize)
            var listUsers: List<ListUsersResponse>? = null
            if (response.isSuccessful) {
                listUsers = response.body()
            }

            val listData = ArrayList<ListUsers>()

            listUsers?.forEach {
                val data = DataMapper.mapListUsersResponseToListUsers(it)
                listData.add(data)
            }

            LoadResult.Page(
                data = listData,
                prevKey = if (page == 0) null else page - 10,
                nextKey = if (listUsers?.isEmpty() == true) null else page + 10
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListUsers>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(10)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(10)
        }
    }
}