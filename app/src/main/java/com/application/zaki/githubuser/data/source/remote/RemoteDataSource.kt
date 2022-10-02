package com.application.zaki.githubuser.data.source.remote

import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    fun getDetailUser(username: String): Flow<NetworkResult<DetailUserResponse>> {
        return flow {
            val response = apiService.getDetailUser(username)

            emit(NetworkResult.Loading(null))

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    emit(NetworkResult.Success(data))
                } else {
                    emit(NetworkResult.Empty)
                }
            } else {
                emit(NetworkResult.Error(response.message()))
            }
        }
            .catch {
                emit(NetworkResult.Error(it.message.toString()))
            }
            .flowOn(Dispatchers.IO)
    }
}