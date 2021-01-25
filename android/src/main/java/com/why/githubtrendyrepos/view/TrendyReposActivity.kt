package com.why.githubtrendyrepos.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.setContent
import com.why.githubtrendyrepos.theme.MyTheme

class TrendyReposActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
            }
        }
    }
}
