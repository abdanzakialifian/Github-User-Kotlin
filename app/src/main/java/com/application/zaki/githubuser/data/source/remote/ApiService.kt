package com.application.zaki.githubuser.data.source.remote

import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(
        @Query("q") username: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Response<UsersResponse>

    @GET("users")
    suspend fun getListUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<ListUsersResponse>>

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): Response<DetailUserResponse>
}