package com.why.githubtrendyrepos

import androidx.lifecycle.ViewModel
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.viewmodels.NavigationBarViewModel
import com.why.githubtrendyrepos.viewmodels.NavigationItemViewModel
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.reflection.shouldBeSubtypeOf
import io.kotest.matchers.shouldBe

class NavigationBarViewModelTest : FreeSpec(
    {
        "ctor" {
            val item = NavigationItemViewModel(TRENDING, true) { }
            val defaultItems = m(
                TRENDING to item,
                SETTINGS to NavigationItemViewModel(SETTINGS) {}
            )
            val vm = NavigationBarViewModel()

            vm::class.shouldBeSubtypeOf<ViewModel>()
            vm.navigationItems shouldBe defaultItems
            vm.currentlySelectedItem shouldBe item
            vm.currentlySelectedItem!!.isSelected.shouldBeTrue()
        }

        "select(navigationItem) should select() the navigationItem passed" {
            val navigationBarVm = NavigationBarViewModel()
            val toBeSelectedItem = navigationBarVm.navigationItems(SETTINGS)!!
            val oldSelectedItem = navigationBarVm.currentlySelectedItem!!

            navigationBarVm.onSelect(toBeSelectedItem)

            navigationBarVm.currentlySelectedItem shouldBe toBeSelectedItem
            oldSelectedItem.isSelected.shouldBeFalse()
        }

        "defaultItems should be wired with select()" {
            val navigationBarVm = NavigationBarViewModel()
            val toBeSelectedItem = navigationBarVm.navigationItems(SETTINGS)!!
            val oldSelectedItem = navigationBarVm.currentlySelectedItem!!

            toBeSelectedItem.select()

            navigationBarVm.currentlySelectedItem shouldBe toBeSelectedItem
            oldSelectedItem.isSelected.shouldBeFalse()
        }
    }
)
