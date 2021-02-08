package com.why.githubtrendyrepos.app

import kotlinx.datetime.LocalDate

interface ReposGateway {
    suspend fun getMostStaredReposSince(
        creationDate: LocalDate,
        page: Int
    ): Map<Result, Any>

    fun close()
}
