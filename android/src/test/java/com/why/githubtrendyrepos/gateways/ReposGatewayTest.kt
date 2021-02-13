package com.why.githubtrendyrepos.gateways

import com.why.githubtrendyrepos.app.DataLimitReached
import com.why.githubtrendyrepos.app.NoConnectivity
import com.why.githubtrendyrepos.app.RateLimitExceeded
import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.RepoDeserializer
import com.why.githubtrendyrepos.app.ReposGatewayImpl
import com.why.githubtrendyrepos.app.Result.Error
import com.why.githubtrendyrepos.app.Result.Ok
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.reflection.shouldBeSubtypeOf
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.AndroidClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.feature
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class ReposGatewayTest : FreeSpec(
    {
        "ctor" {
            val gateway = ReposGatewayImpl()

            gateway.baseUrl shouldBe "https://api.github.com"
            gateway.searchReposApi shouldBe "/search/repositories"
        }

        """
           mostStaredReposSinceDateUrl() should return the URL to get the 
           repositories sorted by stars count since the given date 
        """ {
            val chunk = 5
            val formattedDate = "2021-01-02"
            val gateway = ReposGatewayImpl()

            val url = gateway.mostStaredReposSinceDateUrl(formattedDate, chunk)

            url shouldBe "${gateway.baseUrl}${gateway.searchReposApi}" +
                "?q=created:>$formattedDate&sort=stars&order=desc&page=$chunk"
        }

        "httpClient" {
            val gateway = ReposGatewayImpl()

            val httpClient = gateway.httpClient
            httpClient.engine::class.shouldBeSubtypeOf<AndroidClientEngine>()
            val serializer = httpClient.feature(JsonFeature)!!.serializer

            serializer::class.shouldBeSubtypeOf<GsonSerializer>()
        }

        "getMostStaredReposSince()" - {
            val creationDate = LocalDate(2021, 1, 2)

            @Suppress("UNCHECKED_CAST")
            "It should return a list of Repo" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    val r = gateway.getMostStaredReposSince(creationDate, 1)
                    val repos = r[Ok] as List<Repo>
                    val repo = repos[0]

                    repo.name shouldBe "RepoName"
                    repo.description shouldBe ""
                    repo.author shouldBe "author"
                    repo.starsCount shouldBe 28
                    repo.avatarUrl shouldBe "https://avatars.github.com"
                }
            }

            "when rate limit exceeded, it should return an error" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    val r = gateway.getMostStaredReposSince(creationDate, 50)

                    r[Error].shouldBeInstanceOf<RateLimitExceeded>()
                }
            }

            "when requesting more than 1k repos, it should return an error" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    val r = gateway.getMostStaredReposSince(creationDate, 100)

                    r[Error].shouldBeInstanceOf<DataLimitReached>()
                }
            }

            "When server down or no network, It should return NoConnectivity" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    val r = gateway.getMostStaredReposSince(creationDate, -1)

                    r[Error].shouldBeInstanceOf<NoConnectivity>()
                }
            }

            "It should be able to do multiple requests" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    gateway.getMostStaredReposSince(creationDate, 1)
                    gateway.getMostStaredReposSince(creationDate, 1)
                }
            }

            "close() should close the httpClient"{
                val gateway = ReposGatewayImpl(httpClientMock)

                gateway.close()

                runBlocking {
                    shouldThrow<CancellationException> {
                        gateway.getMostStaredReposSince(creationDate, 1)
                    }
                }
            }
        }
    }
) {
    companion object {
        private val reposJson =
            """
                {
                    "items": [
                        {
                            "name": "RepoName",
                            "owner": {
                                "login": "author",
                                "avatar_url": "https://avatars.github.com"
                            },
                            "description": null,
                            "stargazers_count": 28
                        }
                    ]
                }
            """.trimIndent()

        val httpClientMock = HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    registerTypeAdapter(Repo::class.java, RepoDeserializer())
                        .create()
                }
            }
            engine {
                addHandler { request ->
                    val page = request.url.parameters["page"]!!.toInt()

                    when {
                        page == -1 -> throw UnknownHostException()
                        page < 38 -> {
                            val t = ContentType.Application.Json.toString()
                            val headers = headersOf("Content-Type" to listOf(t))
                            TextContent(reposJson, ContentType.Application.Json)
                            respond(reposJson, headers = headers)
                        }
                        page == 50 -> {
                            respondError(
                                HttpStatusCode.Forbidden,
                                reposJson,
                                headersOf("Content-Type" to listOf())
                            )
                        }
                        else -> {
                            respondError(
                                HttpStatusCode.UnprocessableEntity,
                                reposJson,
                                headersOf("Content-Type" to listOf())
                            )
                        }
                    }
                }
            }
        }
    }
}
