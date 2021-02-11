package com.why.githubtrendyrepos.app

interface UseCase {
    suspend fun execute(request: Map<String, Any>): Map<Result, Any>
}