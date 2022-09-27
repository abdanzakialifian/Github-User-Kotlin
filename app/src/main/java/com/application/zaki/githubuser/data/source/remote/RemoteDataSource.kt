package com.application.zaki.githubuser.data.source.remote

import com.application.zaki.githubuser.data.source.remote.response.UserResponse
import com.application.zaki.githubuser.utils.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getUser(username: String): NetworkResult<UserResponse> =
        try {
            val response = apiService.getUser(username)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Empty
                }
            } else {
                NetworkResult.Error(response.message())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.printStackTrace().toString())
        }
}