package com.application.zaki.githubuser.data.source.remote

import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users/{USERNAME}")
    suspend fun getUser(
        @Path("USERNAME") name: String
    ): Response<UserResponse>

    @GET("users")
    suspend fun getListUser(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<ListUsersResponse>>
}