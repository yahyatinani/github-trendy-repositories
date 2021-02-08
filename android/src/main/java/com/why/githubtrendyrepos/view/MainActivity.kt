package com.why.githubtrendyrepos.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.why.githubtrendyrepos.app.ReposGateway
import com.why.githubtrendyrepos.app.ReposGatewayImpl
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.githubtrendyrepos.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(ReposGatewayImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme(isDarkTheme = mainViewModel.isDarkTheme) {
                Screen(mainViewModel)
            }
        }
    }

    class MainViewModelFactory(private val gateway: ReposGateway) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(MainViewModel::class.java))
                throw IllegalArgumentException("Unknown ViewModel class")

            return MainViewModel(gateway) as T
        }
    }
}
