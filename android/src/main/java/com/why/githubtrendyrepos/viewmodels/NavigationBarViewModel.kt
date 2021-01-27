package com.why.githubtrendyrepos.viewmodels

import androidx.lifecycle.ViewModel
import com.github.whyrising.y.concretions.map.PersistentArrayMap
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING

class NavigationBarViewModel : ViewModel() {
    private
    fun defaultItems(): PersistentArrayMap<Pages, NavigationItemViewModel> =
        m(
            TRENDING to NavigationItemViewModel(TRENDING, true, ::onSelect),
            SETTINGS to NavigationItemViewModel(SETTINGS, onSelect = ::onSelect)
        )

    val navigationItems = defaultItems()

    var currentlySelectedItem: NavigationItemViewModel? =
        navigationItems(TRENDING)
        private set

    fun onSelect(navigationItem: NavigationItemViewModel) {
        currentlySelectedItem?.deselect()
        currentlySelectedItem = navigationItem
    }
}
