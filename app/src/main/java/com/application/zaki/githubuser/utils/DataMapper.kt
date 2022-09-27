package com.application.zaki.githubuser.utils

import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
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
}