package com.why.githubtrendyrepos.viewmodels

import androidx.lifecycle.ViewModel
import com.github.whyrising.y.concretions.map.m
import com.why.githubtrendyrepos.app.GetTrendyReposUseCase
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.reflection.shouldBeSubtypeOf
import io.kotest.matchers.shouldBe

class MainViewModelTest : FreeSpec(
    {
        "ctor" {
            val item = NavigationItemViewModel(TRENDING, true) { }
            val defaultItems = m(
                TRENDING to item,
                SETTINGS to NavigationItemViewModel(SETTINGS) {}
            )

            val vm = MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))
            val oldSelectedPage = vm.currentlySelectedPage

            vm::class.shouldBeSubtypeOf<ViewModel>()
            oldSelectedPage shouldBe TRENDING
            vm.isDarkTheme.shouldBeFalse()
            vm.navigationItems shouldBe defaultItems
            vm.navigationItems(oldSelectedPage)!!.isSelected.shouldBeTrue()
        }

        "select(navigationItem)" - {
            """
                It should deselect() the current nav item and set the new
                selected page
            """ {
                val vm =
                    MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))
                val toBeSelectedItem = vm.navigationItems(SETTINGS)!!
                val oldSelectedPage = vm.currentlySelectedPage

                vm.onSelect(toBeSelectedItem)

                vm.currentlySelectedPage shouldBe toBeSelectedItem.page
                vm.navigationItems(oldSelectedPage)!!.isSelected.shouldBeFalse()
            }

            "when passing the the same selected item, it should do nothing" {
                val vm =
                    MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))
                val currentlySelectedItem = vm.navigationItems(TRENDING)!!

                vm.onSelect(currentlySelectedItem)

                currentlySelectedItem.isSelected.shouldBeTrue()
                vm.currentlySelectedPage shouldBe TRENDING
            }
        }

        "defaultItems should be wired with onSelect(navItem)" {
            val vm = MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))
            val toBeSelectedItem = vm.navigationItems(SETTINGS)!!
            val oldSelectedPage = vm.currentlySelectedPage

            toBeSelectedItem.select()

            vm.currentlySelectedPage shouldBe toBeSelectedItem.page
            vm.navigationItems(oldSelectedPage)!!.isSelected.shouldBeFalse()
        }

        "darkThemeOn()" {
            val vm = MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))

            vm.darkThemeOn()

            vm.isDarkTheme.shouldBeTrue()
        }

        "darkThemeOff()" {
            val vm = MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))

            vm.darkThemeOff()

            vm.isDarkTheme.shouldBeFalse()
        }
    }
)
