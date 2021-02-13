package com.why.githubtrendyrepos.viewmodels

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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

        fun makeRepoViewModel(starsCount: Int): RepoViewModel = RepoViewModel(
            "name-example",
            "description description",
            "Author",
            starsCount,
            "https://repo-image-link"
        )

        "formatStarsCount" {
            makeRepoViewModel(523).starsCountStr shouldBe "523"
            makeRepoViewModel(999).starsCountStr shouldBe "999"
            makeRepoViewModel(1000).starsCountStr shouldBe "1k"
            makeRepoViewModel(1200).starsCountStr shouldBe "1.2k"
            makeRepoViewModel(1963).starsCountStr shouldBe "1.9k"
            makeRepoViewModel(2000).starsCountStr shouldBe "2k"
            makeRepoViewModel(2236).starsCountStr shouldBe "2.2k"
            makeRepoViewModel(20459).starsCountStr shouldBe "20.4k"
            makeRepoViewModel(20001).starsCountStr shouldBe "20k"
            makeRepoViewModel(93926).starsCountStr shouldBe "93.9k"
            makeRepoViewModel(100500).starsCountStr shouldBe "100k"
            makeRepoViewModel(101563).starsCountStr shouldBe "101k"
            makeRepoViewModel(171563).starsCountStr shouldBe "171k"
            makeRepoViewModel(542369).starsCountStr shouldBe "542k"
        }
    }
)
