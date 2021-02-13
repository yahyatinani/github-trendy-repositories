package com.why.githubtrendyrepos.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NavigationItemViewModel(
    val page: Pages,
    _isSelected: Boolean = false,
    private val onSelect: (NavigationItemViewModel) -> Unit,
) : ViewModel() {
    var isSelected by mutableStateOf(_isSelected)
        private set

    fun select() {
        isSelected = true
        onSelect(this)
    }

    fun deselect() {
        isSelected = false
    }

    override fun equals(other: Any?): Boolean = when {
        other !is NavigationItemViewModel -> false
        this === other -> true
        this.page == other.page && this.isSelected == other.isSelected -> true
        else -> false
    }

    override fun hashCode(): Int {
        var result = page.hashCode()
        result = 31 * result + onSelect.hashCode()
        return result
    }
}
