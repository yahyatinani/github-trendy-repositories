package com.why.githubtrendyrepos.viewmodels

import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.ReposGateway
import com.why.githubtrendyrepos.app.Result
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class ReposGatewayMock() : ReposGateway {

    val repo: Repo = Repo(
        name = "repo-example",
        description = "cool repo, try it out!!",
        starsCount = 185,
        author = "author",
        avatarUrl = ""
    )

    override suspend fun getMostStaredReposSince(
        creationDate: LocalDate, page: Int
    ): Map<Result, Any> {
        return when {
            page == 1 || creationDate == creationDate() ->
                mapOf(Result.Ok to listOf(repo))
            else -> mapOf()
        }
    }

    private fun creationDate() = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date.minus(DatePeriod(days = 30))

    override fun close() {}
}
