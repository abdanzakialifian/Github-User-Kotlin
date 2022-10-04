package com.application.zaki.githubuser.utils

import com.application.zaki.githubuser.data.source.remote.response.DetailUserResponse
import com.application.zaki.githubuser.data.source.remote.response.ListUsersResponse
import com.application.zaki.githubuser.data.source.remote.response.RepositoriesUserResponse
import com.application.zaki.githubuser.data.source.remote.response.UsersItemResponse
import com.application.zaki.githubuser.domain.model.*

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

    fun mapRepositoriesUserResponseToRepositoriesUser(input: RepositoriesUserResponse?): RepositoriesUser {
        val license = License(
            name = input?.license?.name,
            spdxId = input?.license?.spdxId,
            key = input?.license?.key,
            url = input?.license?.url,
            nodeId = input?.license?.nodeId
        )

        val owner = Owner(
            gistsUrl = input?.owner?.gistsUrl,
            reposUrl = input?.owner?.reposUrl,
            followingUrl = input?.owner?.followingUrl,
            starredUrl = input?.owner?.starredUrl,
            login = input?.owner?.login,
            followersUrl = input?.owner?.followersUrl,
            type = input?.owner?.type,
            url = input?.owner?.url,
            subscriptionsUrl = input?.owner?.subscriptionsUrl,
            receivedEventsUrl = input?.owner?.receivedEventsUrl,
            avatarUrl = input?.owner?.avatarUrl,
            eventsUrl = input?.owner?.eventsUrl,
            htmlUrl = input?.owner?.htmlUrl,
            siteAdmin = input?.owner?.siteAdmin,
            id = input?.owner?.id,
            gravatarId = input?.owner?.gravatarId,
            nodeId = input?.owner?.nodeId,
            organizationsUrl = input?.owner?.organizationsUrl
        )

        return RepositoriesUser(
            allowForking = input?.allowForking,
            stargazersCount = input?.stargazersCount,
            isTemplate = input?.isTemplate,
            pushedAt = input?.pushedAt,
            subscriptionUrl = input?.subscriptionUrl,
            language = input?.language,
            branchesUrl = input?.branchesUrl,
            issueCommentUrl = input?.issueCommentUrl,
            labelsUrl = input?.labelsUrl,
            subscribersUrl = input?.subscribersUrl,
            releasesUrl = input?.releasesUrl,
            svnUrl = input?.svnUrl,
            id = input?.id,
            forks = input?.forks,
            archiveUrl = input?.archiveUrl,
            gitRefsUrl = input?.gitRefsUrl,
            forksUrl = input?.forksUrl,
            visibility = input?.visibility,
            statusesUrl = input?.statusesUrl,
            sshUrl = input?.sshUrl,
            license = license,
            fullName = input?.fullName,
            size = input?.size,
            languagesUrl = input?.languagesUrl,
            htmlUrl = input?.htmlUrl,
            collaboratorsUrl = input?.collaboratorsUrl,
            cloneUrl = input?.cloneUrl,
            name = input?.name,
            pullsUrl = input?.pullsUrl,
            defaultBranch = input?.defaultBranch,
            hooksUrl = input?.hooksUrl,
            treesUrl = input?.treesUrl,
            tagsUrl = input?.tagsUrl,
            jsonMemberPrivate = input?.jsonMemberPrivate,
            contributorsUrl = input?.contributorsUrl,
            hasDownloads = input?.hasDownloads,
            notificationsUrl = input?.notificationsUrl,
            openIssuesCount = input?.openIssuesCount,
            description = input?.description,
            createdAt = input?.createdAt,
            watchers = input?.watchers,
            keysUrl = input?.keysUrl,
            deploymentsUrl = input?.deploymentsUrl,
            hasProjects = input?.hasProjects,
            archived = input?.archived,
            hasWiki = input?.hasWiki,
            updatedAt = input?.updatedAt,
            commentsUrl = input?.commentsUrl,
            stargazersUrl = input?.stargazersUrl,
            disabled = input?.disabled,
            gitUrl = input?.gitUrl,
            hasPages = input?.hasPages,
            owner = owner,
            commitsUrl = input?.commitsUrl,
            compareUrl = input?.compareUrl,
            gitCommitsUrl = input?.gitCommitsUrl,
            topics = input?.topics,
            blobsUrl = input?.blobsUrl,
            gitTagsUrl = input?.gitTagsUrl,
            mergesUrl = input?.mergesUrl,
            downloadsUrl = input?.downloadsUrl,
            hasIssues = input?.hasIssues,
            webCommitSignoffRequired = input?.webCommitSignoffRequired,
            url = input?.url,
            contentsUrl = input?.contentsUrl,
            mirrorUrl = input?.mirrorUrl,
            milestonesUrl = input?.milestonesUrl,
            teamsUrl = input?.teamsUrl,
            fork = input?.fork,
            issuesUrl = input?.issuesUrl,
            eventsUrl = input?.eventsUrl,
            issueEventsUrl = input?.issueEventsUrl,
            assigneesUrl = input?.assigneesUrl,
            openIssues = input?.openIssues,
            watchersCount = input?.watchersCount,
            nodeId = input?.nodeId,
            homepage = input?.homepage,
            forksCount = input?.forksCount
        )
    }
}