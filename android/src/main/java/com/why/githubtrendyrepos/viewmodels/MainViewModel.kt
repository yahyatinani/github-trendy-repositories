package com.why.githubtrendyrepos.viewmodels

import androidx.lifecycle.ViewModel
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING

class MainViewModel : ViewModel() {

    private fun defaultItems() = m(
        TRENDING to NavigationItemViewModel(TRENDING, true, ::onSelect),
        SETTINGS to NavigationItemViewModel(SETTINGS, onSelect = ::onSelect)
    )

    val navigationItems = defaultItems()

    var currentlySelectedPage: Pages = TRENDING
        private set

    fun onSelect(navigationItem: NavigationItemViewModel) {
        navigationItems(currentlySelectedPage)?.deselect()
        currentlySelectedPage = navigationItem.page
    }
}
