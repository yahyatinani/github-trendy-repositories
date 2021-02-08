package com.why.githubtrendyrepos.app

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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
import java.lang.reflect.Type
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

    private suspend fun handle(action: suspend () -> Any): Map<Result, Any> {
        return try {
            mapOf(Ok to action())
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

    override suspend fun getMostStaredReposSince(
        creationDate: LocalDate,
        page: Int
    ): Map<Result, Any> {
        val formattedDate = creationDate.toString()
        val urlString = mostStaredReposSinceDateUrl(formattedDate, page)

        return handle { httpClient.get<Repos>(urlString).items }
    }

    override fun close() {
        httpClient.close()
    }

    companion object {
        private fun createHttpClient(): HttpClient = HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    registerTypeAdapter(Repo::class.java, RepoDeserializer())
                        .create()
                }
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

class RepoDeserializer : JsonDeserializer<Any> {
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Repo {
        val repo = jsonElement.asJsonObject

        val name = repo.get("name").asString
        val element = repo.get("description")
        val desc = when {
            element.isJsonNull -> ""
            else -> element.asString
        }
        val owner = repo.get("owner")
        val author = owner.asJsonObject.get("login").asString
        val avatar = owner.asJsonObject.get("avatar_url").asString
        val starsCount = repo.get("stargazers_count").asInt

        return Repo(
            name = name,
            description = desc,
            author = author,
            avatarUrl = avatar,
            starsCount = starsCount
        )
    }
}
