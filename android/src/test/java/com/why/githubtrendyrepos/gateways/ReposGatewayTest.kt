package com.why.githubtrendyrepos.gateways

import com.why.githubtrendyrepos.app.GatewayError.NoConnectivity
import com.why.githubtrendyrepos.app.ReposGatewayImpl
import com.why.githubtrendyrepos.app.Result.Error
import com.why.githubtrendyrepos.app.Result.Ok
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.reflection.shouldBeSubtypeOf
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import io.ktor.http.content.*
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
                    val repos = r[Ok] as List<Map<String, Any>>
                    val repo = repos[0]
                    val owner = repo["owner"] as Map<String, Any>

                    repo["description"] shouldBe "Cool repo"
                    repo["stargazers_count"] shouldBe 28
                    owner["avatar_url"] shouldBe "https://avatars.github.com"
                }
            }

//            "when reach limit of data available, return an error" {
//                val gateway = ReposGatewayImpl(httpClientMock)
//
//                runBlocking {
//                    val r = gateway.getMostStaredReposSince(creationDate, 50)
//
//                    r[Error] shouldBe DataLimitReached
//                }
//            }
//
            "When server down or no network, It should return NoConnectivity" {
                val gateway = ReposGatewayImpl(httpClientMock)

                runBlocking {
                    val r = gateway.getMostStaredReposSince(creationDate, -1)

                    r[Error] shouldBe NoConnectivity
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
        private val reposJson = """
            {
                "items": [
                    {
                        "name": "reanimation",
                        "owner": {
                            "avatar_url": "https://avatars.github.com"
                        },
                        "description": "Cool repo",
                        "stargazers_count": 28
                    }
                ]
            }
        """.trimIndent()

        val httpClientMock = HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = GsonSerializer()
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
                        else -> TODO()
                    }

                }
            }
        }
    }
}
