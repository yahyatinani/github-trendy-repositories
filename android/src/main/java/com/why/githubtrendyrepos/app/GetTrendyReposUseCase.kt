package com.why.githubtrendyrepos.app

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class GetTrendyReposUseCase(private val gateway: ReposGateway) : UseCase {

    val repoCreationDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date.minus(DatePeriod(days = 30))

    override suspend fun execute(request: Map<String, Any>): Map<Result, Any> {
        val page = request["page"] as Int

        return gateway.getMostStaredReposSince(repoCreationDate, page)
    }
}
