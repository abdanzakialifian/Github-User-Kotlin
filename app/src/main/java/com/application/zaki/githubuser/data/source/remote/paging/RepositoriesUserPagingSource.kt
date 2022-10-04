package com.application.zaki.githubuser.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.zaki.githubuser.data.source.remote.ApiService
import com.application.zaki.githubuser.data.source.remote.response.RepositoriesUserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoriesUserPagingSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, RepositoriesUserResponse>() {

    private var username: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoriesUserResponse> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getRepositoriesUser(username ?: "", position, params.loadSize)
            val data = response.body()
            val listRepositories = ArrayList<RepositoriesUserResponse>()

            if (response.isSuccessful) {
                data?.let {
                    listRepositories.addAll(it)
                }
            }

            LoadResult.Page(
                data = listRepositories,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (listRepositories.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoriesUserResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    fun setUsername(username: String) {
        this.username = username
    }
}