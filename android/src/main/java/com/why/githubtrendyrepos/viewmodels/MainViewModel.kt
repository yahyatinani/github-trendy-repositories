package com.why.githubtrendyrepos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.app.ReposGateway
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING

class MainViewModel(val gateway: ReposGateway) : ViewModel() {

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
        navigationItems(currentlySelectedPage)?.deselect()
        currentlySelectedPage = navigationItem.page
    }

    fun darkThemeOn() {
        isDarkTheme = true
    }

    fun darkThemeOff() {
        isDarkTheme = false
    }
}
