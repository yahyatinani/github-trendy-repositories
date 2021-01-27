package com.why.githubtrendyrepos.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.githubtrendyrepos.viewmodels.NavigationBarViewModel

class MainActivity : ComponentActivity() {
    private val navigationBarVm by viewModels<NavigationBarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                Screen(navigationBarVm)
            }
        }
    }
}
