package com.application.zaki.githubuser.utils

import com.application.zaki.githubuser.data.source.local.entity.UserEntity
import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.RepositoriesUserResponse
import com.application.zaki.githubuser.data.source.remote.response.UsersItemResponse
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers
import com.application.zaki.githubuser.domain.model.RepositoriesUser
import com.application.zaki.githubuser.domain.model.User

object DataMapper {
    fun mapListUsersResponseToListUsers(input: ListUsersResponse?): ListUsers = ListUsers(
        login = input?.login,
        avatarUrl = input?.avatarUrl,
        htmlUrl = input?.htmlUrl,
        id = input?.id,
    )

    fun mapUsersItemResponseToListUsers(input: UsersItemResponse?): ListUsers = ListUsers(
        login = input?.login,
        avatarUrl = input?.avatarUrl,
        id = input?.id,
        htmlUrl = input?.htmlUrl,
    )

    fun mapDetailUserResponseToDetailUser(input: DetailUserResponse?): DetailUser = DetailUser(
        bio = input?.bio,
        login = input?.login,
        company = input?.company,
        id = input?.id,
        publicRepos = input?.publicRepos,
        followers = input?.followers,
        avatarUrl = input?.avatarUrl,
        htmlUrl = input?.htmlUrl,
        following = input?.following,
        name = input?.name,
    )

    fun mapRepositoriesUserResponseToRepositoriesUser(input: RepositoriesUserResponse?): RepositoriesUser =
        RepositoriesUser(
            visibility = input?.visibility,
            name = input?.name,
            id = input?.id,
            language = input?.language,
        )

    fun mapListUserEntityToListUser(input: List<UserEntity>): List<User> = input.map { map ->
        User(id = map.id, image = map.image, username = map.username)
    }

    fun mapUserToUserEntity(input: User): UserEntity =
        UserEntity(id = input.id ?: 0, image = input.image ?: "", username = input.username ?: "")
}