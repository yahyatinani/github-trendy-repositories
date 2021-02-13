package com.why.githubtrendyrepos.viewmodels

import androidx.lifecycle.ViewModel
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.reflection.shouldBeSubtypeOf
import io.kotest.matchers.shouldBe

class
NavigationItemViewModelTest : FreeSpec(
    {
        "ctor" {
            val vm = NavigationItemViewModel(Pages.SETTINGS, true) {}

            vm::class.shouldBeSubtypeOf<ViewModel>()
            vm.page shouldBe Pages.SETTINGS
            vm.isSelected.shouldBeTrue()
        }

        "select() should set `isSelected` to true and call onSelect()" {
            var i = 0
            val vm = NavigationItemViewModel(Pages.SETTINGS) {
                i++
            }

            vm.select()

            vm.isSelected.shouldBeTrue()
            i shouldBeExactly 1
        }

        "deselect() should set `isSelected` to to false" {
            val vm = NavigationItemViewModel(Pages.SETTINGS, true) { }

            vm.deselect()

            vm.isSelected.shouldBeFalse()
        }

        "equals()" {
            val vm1 = NavigationItemViewModel(Pages.TRENDING) {}
            val vm2 = NavigationItemViewModel(Pages.SETTINGS) {}
            val vm3 = NavigationItemViewModel(Pages.SETTINGS, true) {}

            (vm1 == vm1).shouldBeTrue()
            vm1.equals(1).shouldBeFalse()
            (vm2 == vm3).shouldBeFalse()
        }
    }
)
