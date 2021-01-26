package com.why.githubtrendyrepos.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.template.compose.R

private const val placeholderDescriptionText = "It does a lot of cool stuff, " +
    "try it!! It does a lot of cool stuff, try it!! It does a lot of cool " +
    "stuff, try it!! It does a lot of cool stuff, try it!!"

@Composable
private fun ImageText(
    text: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    imgSize: Dp = 24.dp,
    spaceWidth: Dp = 8.dp,
    textStyle: TextStyle
) {
    Row(modifier = modifier) {
        Icon(
            imageVector = imageVector,
            modifier = Modifier
                .size(imgSize)
                .align(Alignment.CenterVertically)
        )
        //TODO: Put the repos image in here.
        Spacer(modifier = Modifier.width(spaceWidth))
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun RepoItem() {
    val typography = MaterialTheme.typography

    Surface {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(text = "example-api-name", style = typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = placeholderDescriptionText,
                maxLines = 2,
                style = typography.caption,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ImageText(
                    text = "Author",
                    modifier = Modifier
                        .weight(weight = 1f)
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Default.Image,
                    textStyle = typography.overline
                )

                ImageText(
                    text = "1.5k",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                    imageVector = Icons.Default.Star,
                    imgSize = 16.dp,
                    spaceWidth = 4.dp,
                    textStyle = typography.overline
                )
            }
        }
    }
}

@Composable
fun TopBar(type: Typography) {
    Column {
        TopAppBar(
            elevation = 1.dp,
            title = {
                Text(
                    text = stringResource(R.string.app_title),
                    style = type.h6.merge(
                        TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

private fun toImageVector(item: BottomBarItem) =
    when (item.status) {
        BottomBarItems.TRENDING -> Icons.Filled.Star
        BottomBarItems.SETTINGS -> Icons.Filled.Settings
    }

@Composable
fun BottomBar(navigationItems: List<BottomBarItem>) {
    val colors = MaterialTheme.colors
    val allItemsStates = mutableListOf<MutableState<Boolean>>()

    Column {
        Divider(modifier = Modifier.fillMaxWidth())
        BottomNavigation(elevation = 1.dp) {
            navigationItems.forEach { item ->
                val itemState = remember { mutableStateOf(item.isSelected) }
                allItemsStates.add(itemState)

                BottomNavigationItem(
                    selectedContentColor = colors.secondary,
                    unselectedContentColor = colors.onPrimary.copy(
                        alpha = ContentAlpha.medium
                    ),
                    label = {
                        Text(text = item.label)
                    },
                    icon = {
                        Icon(imageVector = toImageVector(item))
                    },
                    selected = itemState.value,
                    onClick = {
                        if (!itemState.value) {
                            allItemsStates.forEach { it.value = false }
                            itemState.value = true
                            //TODO: Change content of the screen
                        }
                    }
                )
            }
        }
    }
}

enum class BottomBarItems {
    TRENDING,
    SETTINGS
}

data class BottomBarItem(
    val label: String,
    val status: BottomBarItems,
    val isSelected: Boolean = false
)

@Composable
fun Screen() {
    val type = MaterialTheme.typography

    val items = listOf(
        BottomBarItem("Trending", BottomBarItems.TRENDING, true),
        BottomBarItem("Settings", BottomBarItems.SETTINGS),
    )

    Scaffold(
        topBar = { TopBar(type) },
        bottomBar = { BottomBar(items) }
    ) {}
}

@Composable
@Preview(showBackground = true, name = "Repo Item")
fun RepoItemPreview() {
    MyTheme {
        RepoItem()
    }
}

@Composable
@Preview(showBackground = true, name = "Repo Item - Dark theme")
fun RepoItemDarkPreview() {
    MyTheme(isDarkTheme = true) {
        RepoItem()
    }
}

@Composable
@Preview(showBackground = true)
fun TrendyReposPreview() {
    MyTheme {
        Screen()
    }
}

@Composable
@Preview(showBackground = true, name = "Trendy Repos - Dark theme")
fun TrendyReposDarkPreview() {
    MyTheme(isDarkTheme = true) {
        Screen()
    }
}
