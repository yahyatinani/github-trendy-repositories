package com.why.githubtrendyrepos.app

data class Repo(
    val name: String,
    val description: String,
    val stargazers_count: Int,
    val owner: Owner,
)
