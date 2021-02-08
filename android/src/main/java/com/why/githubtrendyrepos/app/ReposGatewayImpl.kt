package com.why.githubtrendyrepos.app

import com.why.githubtrendyrepos.app.GatewayError.DataLimitReached
import com.why.githubtrendyrepos.app.GatewayError.NoConnectivity
import com.why.githubtrendyrepos.app.Result.Error
import com.why.githubtrendyrepos.app.Result.Ok
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import java.net.UnknownHostException

class ReposGatewayImpl(
    internal val httpClient: HttpClient = createHttpClient()
) : ReposGateway {
    internal val baseUrl = "https://api.github.com"

    internal val searchReposApi = "/search/repositories"

    /**
     * @param formattedDate : ex: 2021-01-02
     **/
    fun mostStaredReposSinceDateUrl(formattedDate: String, chunk: Int): String =
        "$baseUrl$searchReposApi?q=created:>$formattedDate&sort=stars" +
            "&order=desc&page=$chunk"

    override suspend fun getMostStaredReposSince(
        creationDate: LocalDate,
        page: Int
    ): Map<Result, Any> {
        val formattedDate = creationDate.toString()
        val urlString = mostStaredReposSinceDateUrl(formattedDate, page)

        return try {
            val result = httpClient.get<Map<String, Any>>(urlString)
            @Suppress("UNCHECKED_CAST")
            mapOf(Ok to result["items"]!!)
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.UnprocessableEntity ->
                    mapOf(Error to DataLimitReached)
                else -> {
                    TODO()
                }
            }
        } catch (e: UnknownHostException) {
            mapOf(Error to NoConnectivity)
        }
    }

    override fun close() {
        httpClient.close()
    }

    companion object {
        private fun createHttpClient(): HttpClient = HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }
}

enum class Result {
    Ok,
    Error,
}

enum class GatewayError {
    DataLimitReached,
    NoConnectivity,
}
