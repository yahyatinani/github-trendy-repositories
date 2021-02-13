package com.why.githubtrendyrepos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.Result.Error
import com.why.githubtrendyrepos.app.Result.Ok
import com.why.githubtrendyrepos.app.UseCase
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val useCase: UseCase) : ViewModel() {

    private fun defaultItems() = m(
        TRENDING to NavigationItemViewModel(TRENDING, true, ::onSelect),
        SETTINGS to NavigationItemViewModel(SETTINGS, onSelect = ::onSelect)
    )

    var isDarkTheme: Boolean by mutableStateOf(false)
        private set

    val navigationItems = defaultItems()

    var currentlySelectedPage: Pages by mutableStateOf(TRENDING)
        private set

    fun onSelect(navigationItem: NavigationItemViewModel) {
        if (currentlySelectedPage != navigationItem.page) {
            navigationItems(currentlySelectedPage)?.deselect()
            currentlySelectedPage = navigationItem.page
        }
    }

    fun darkThemeOn() {
        isDarkTheme = true
    }

    fun darkThemeOff() {
        isDarkTheme = false
    }

    fun reposPagination(): Flow<PagingData<RepoViewModel>> =
        Pager<Int, RepoViewModel>(PagingConfig(pageSize = 20)) {
            ReposPagingSource(useCase)
        }.flow

    class ReposPagingSource(private val useCase: UseCase) :
        PagingSource<Int, RepoViewModel>() {

        override
        fun getRefreshKey(state: PagingState<Int, RepoViewModel>): Int? {
            TODO("Not yet implemented")
        }

        override suspend fun load(
            params: LoadParams<Int>
        ): LoadResult<Int, RepoViewModel> {
            val page = params.key ?: 1
            val response = useCase.execute(mapOf("page" to page))
            val repos = response[Ok]
                ?: return LoadResult.Error(response[Error] as Throwable)

            @Suppress("UNCHECKED_CAST")
            return LoadResult.Page(
                data = (repos as List<Repo>).map { toRepoViewModel(it) },
                prevKey = if (page == 1) null else page - 1,
                nextKey = page.inc()
            )
        }
    }

    companion object {
        fun toRepoViewModel(repo: Repo) = RepoViewModel(
            name = repo.name,
            description = repo.description,
            author = repo.author,
            starsCount = repo.starsCount,
            authorAvatarUrl = repo.avatarUrl
        )
    }
}
