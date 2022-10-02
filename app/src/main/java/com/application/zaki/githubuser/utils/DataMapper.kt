package com.application.zaki.githubuser.utils

import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.UsersItemResponse
import com.application.zaki.githubuser.domain.model.DetailUser
import com.application.zaki.githubuser.domain.model.ListUsers

object DataMapper {
    fun mapListUsersResponseToListUsers(input: ListUsersResponse?): ListUsers {
        return ListUsers(
            gistsUrl = input?.gistsUrl,
            reposUrl = input?.reposUrl,
            followingUrl = input?.followingUrl,
            starredUrl = input?.starredUrl,
            login = input?.login,
            followersUrl = input?.followersUrl,
            type = input?.type,
            url = input?.url,
            subscriptionsUrl = input?.subscriptionsUrl,
            receivedEventsUrl = input?.receivedEventsUrl,
            avatarUrl = input?.avatarUrl,
            eventsUrl = input?.eventsUrl,
            htmlUrl = input?.htmlUrl,
            siteAdmin = input?.siteAdmin,
            id = input?.id,
            gravatarId = input?.gravatarId,
            nodeId = input?.nodeId,
            organizationsUrl = input?.organizationsUrl
        )
    }

    fun mapUsersItemResponseToListUsers(input: UsersItemResponse?): ListUsers {
        return ListUsers(
            gistsUrl = input?.gistsUrl,
            reposUrl = input?.reposUrl,
            followingUrl = input?.followingUrl,
            starredUrl = input?.starredUrl,
            login = input?.login,
            followersUrl = input?.followersUrl,
            type = input?.type,
            url = input?.url,
            subscriptionsUrl = input?.subscriptionsUrl,
            receivedEventsUrl = input?.receivedEventsUrl,
            avatarUrl = input?.avatarUrl,
            eventsUrl = input?.eventsUrl,
            htmlUrl = input?.htmlUrl,
            siteAdmin = input?.siteAdmin,
            id = input?.id,
            gravatarId = input?.gravatarId,
            nodeId = input?.nodeId,
            organizationsUrl = input?.organizationsUrl
        )
    }

    fun mapDetailUserResponseToDetailUser(input: DetailUserResponse?): DetailUser {
        return DetailUser(
            gistsUrl = input?.gistsUrl,
            reposUrl = input?.reposUrl,
            followingUrl = input?.followingUrl,
            twitterUsername = input?.twitterUsername,
            bio = input?.bio,
            createdAt = input?.createdAt,
            login = input?.login,
            type = input?.type,
            blog = input?.blog,
            subscriptionsUrl = input?.subscriptionsUrl,
            updatedAt = input?.updatedAt,
            siteAdmin = input?.siteAdmin,
            company = input?.company,
            id = input?.id,
            publicRepos = input?.publicRepos,
            gravatarId = input?.gravatarId,
            email = input?.email,
            organizationsUrl = input?.organizationsUrl,
            hireable = input?.hireable,
            starredUrl = input?.starredUrl,
            followersUrl = input?.followersUrl,
            publicGists = input?.publicGists,
            url = input?.followersUrl,
            receivedEventsUrl = input?.receivedEventsUrl,
            followers = input?.followers,
            avatarUrl = input?.avatarUrl,
            eventsUrl = input?.eventsUrl,
            htmlUrl = input?.htmlUrl,
            following = input?.following,
            name = input?.name,
            location = input?.location,
            nodeId = input?.nodeId
        )
    }
}