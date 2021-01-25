package com.why.githubtrendyrepos.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.setContent
import com.why.githubtrendyrepos.theme.MyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                Greeting("Android ${Build.VERSION.SDK_INT}")
            }
        }
    }
}
