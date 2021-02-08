package com.why.githubtrendyrepos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.app.Repo
import com.why.githubtrendyrepos.app.ReposGateway
import com.why.githubtrendyrepos.app.Result.Ok
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class MainViewModel(private val gateway: ReposGateway) : ViewModel() {

    private fun defaultItems() = m(
        TRENDING to NavigationItemViewModel(TRENDING, true, ::onSelect),
        SETTINGS to NavigationItemViewModel(SETTINGS, onSelect = ::onSelect)
    )

    val repos = mutableStateListOf<RepoViewModel>()

    var isDarkTheme: Boolean by mutableStateOf(false)
        private set

    val navigationItems = defaultItems()

    var currentlySelectedPage: Pages by mutableStateOf(TRENDING)
        private set

    fun onSelect(navigationItem: NavigationItemViewModel) {
        navigationItems(currentlySelectedPage)?.deselect()
        currentlySelectedPage = navigationItem.page
    }

    fun darkThemeOn() {
        isDarkTheme = true
    }

    fun darkThemeOff() {
        isDarkTheme = false
    }

    fun loadRepos() {
        viewModelScope.launch {
            val now = Clock.System.now()
            val creationDate = now
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.minus(DatePeriod(days = 30))

            val r = gateway.getMostStaredReposSince(creationDate, 1)

            @Suppress("UNCHECKED_CAST")
            val l = (r[Ok] as List<Repo>).map { repo ->
                RepoViewModel(
                    name = repo.name,
                    description = repo.description,
                    author = repo.author,
                    starsCount = repo.starsCount,
                    authorAvatarUrl = repo.avatarUrl
                )
            }

            repos.addAll(l)
        }
    }
}
