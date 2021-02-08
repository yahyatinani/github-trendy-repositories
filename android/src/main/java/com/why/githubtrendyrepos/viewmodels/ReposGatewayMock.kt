package com.why.githubtrendyrepos.viewmodels

import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.ReposGateway
import com.why.githubtrendyrepos.app.Result
import com.why.githubtrendyrepos.app.Result.Ok
import kotlinx.datetime.LocalDate

class ReposGatewayMock : ReposGateway {
    override suspend fun getMostStaredReposSince(
        creationDate: LocalDate, page: Int
    ): Map<Result, Any> = mapOf(
        Ok to listOf(
            Repo(
                name = "repo-example",
                description = "cool repo, try it out!!",
                starsCount = 185,
                author = "author",
                avatarUrl = ""
            )
        )
    )

    override fun close() {}
}
