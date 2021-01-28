package com.why.githubtrendyrepos.viewmodels

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

private fun makeRepoViewModel(starsCount: Int): RepoViewModel = RepoViewModel(
    "name-example",
    "description description",
    "Author",
    starsCount,
    "https://repo-image-link"
)

class RepoViewModelTest : FreeSpec(
    {
        "ctor"{
            val vm = RepoViewModel(
                "name-example",
                "description description",
                "Author",
                1500,
                "https://repo-image-link"
            )
        }

        "formatStarsCount" {
            makeRepoViewModel(523).formatStarsCount() shouldBe "523"
            makeRepoViewModel(999).formatStarsCount() shouldBe "999"
            makeRepoViewModel(1000).formatStarsCount() shouldBe "1k"
            makeRepoViewModel(1200).formatStarsCount() shouldBe "1.2k"
            makeRepoViewModel(1963).formatStarsCount() shouldBe "1.9k"
            makeRepoViewModel(2000).formatStarsCount() shouldBe "2k"
            makeRepoViewModel(2236).formatStarsCount() shouldBe "2.2k"
            makeRepoViewModel(20459).formatStarsCount() shouldBe "20.4k"
            makeRepoViewModel(20001).formatStarsCount() shouldBe "20k"
            makeRepoViewModel(93926).formatStarsCount() shouldBe "93.9k"
            makeRepoViewModel(100500).formatStarsCount() shouldBe "100k"
            makeRepoViewModel(101563).formatStarsCount() shouldBe "101k"
            makeRepoViewModel(171563).formatStarsCount() shouldBe "171k"
            makeRepoViewModel(542369).formatStarsCount() shouldBe "542k"
        }
    }
)
