package com.why.githubtrendyrepos.usecases

import com.why.githubtrendyrepos.app.GetTrendyReposUseCase
import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.Result.Ok
import com.why.githubtrendyrepos.viewmodels.ReposGatewayMock
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class GetTrendyReposUseCaseTest : FreeSpec(
    {
        val gateway = ReposGatewayMock()
        "ctor" {
            val useCase = GetTrendyReposUseCase(gateway)
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            val expectedDate = today.minus(DatePeriod(days = 30))

            useCase.repoCreationDate shouldBe expectedDate
        }

        "execute(page)" {
            val useCase = GetTrendyReposUseCase(gateway)
            val request = mapOf("page" to 1)

            val response = useCase.execute(request)

            @Suppress("UNCHECKED_CAST")
            val listRepos = response[Ok] as List<Repo>

            listRepos.size shouldBeExactly 1
            listRepos.shouldContain(gateway.repo)
        }
    }
)
