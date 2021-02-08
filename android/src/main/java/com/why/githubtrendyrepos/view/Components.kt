package com.why.githubtrendyrepos.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.githubtrendyrepos.viewmodels.MainViewModel
import com.why.githubtrendyrepos.viewmodels.NavigationItemViewModel
import com.why.githubtrendyrepos.viewmodels.Pages
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import com.why.githubtrendyrepos.viewmodels.RepoViewModel
import com.why.githubtrendyrepos.viewmodels.ReposGatewayMock
import com.why.template.compose.R

@Composable
fun NetworkImage(
    url: String?,
    modifier: Modifier,
    placeholder: @Composable () -> Unit
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var isImageReady by remember { mutableStateOf(false) }

    onCommit(url) {
        val picasso = Picasso.get()

        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                isImageReady = false
            }

            override fun onBitmapFailed(
                e: Exception?,
                errorDrawable: Drawable?
            ) {
                isImageReady = false
            }

            override fun onBitmapLoaded(
                bitmap: Bitmap?,
                from: Picasso.LoadedFrom?
            ) {
                image = bitmap?.asImageBitmap()
                isImageReady = true
            }
        }

        picasso
            .load(url)
            .resize(24, 24)
            .centerCrop()
            .into(target)

        onDispose {
            image = null
            isImageReady = false
            picasso.cancelRequest(target)
        }
    }

    when {
        isImageReady -> Image(image!!, modifier = modifier)
        else -> placeholder()
    }
}

@Composable
private fun ImageText(
    text: String,
    modifier: Modifier = Modifier,
    spaceWidth: Dp = 8.dp,
    textStyle: TextStyle,
    image: @Composable () -> Unit,
) {
    Row(modifier = modifier) {
        image()
        Spacer(modifier = Modifier.width(spaceWidth))
        Text(
            text = text,
            style = textStyle,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun RepoItem(repoViewModel: RepoViewModel) {
    val typography = MaterialTheme.typography

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(text = repoViewModel.name, style = typography.subtitle1)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = repoViewModel.description,
                maxLines = 2,
                style = typography.caption,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ImageText(
                    text = repoViewModel.author,
                    modifier = Modifier
                        .weight(weight = 1f)
                        .align(alignment = Alignment.CenterVertically),
                    textStyle = typography.overline
                ) {
                    val modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                    NetworkImage(
                        url = repoViewModel.authorAvatarUrl,
                        modifier = modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            modifier = modifier
                        )
                    }
                }

                ImageText(
                    text = repoViewModel.starsCountStr,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically),
                    spaceWidth = 4.dp,
                    textStyle = typography.overline
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
private fun topBarTitle(mainViewModel: MainViewModel) =
    when (mainViewModel.currentlySelectedPage) {
        TRENDING -> stringResource(R.string.top_bar_trending_title)
        SETTINGS -> stringResource(R.string.top_bar_settings_title)
    }

@Composable
fun TopBar(mainViewModel: MainViewModel) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = topBarTitle(mainViewModel),
                    style = MaterialTheme.typography.h6.merge(
                        TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 17.sp,
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            elevation = 1.dp
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun toImageVector(page: Pages): Pair<String, ImageVector> =
    when (page) {
        TRENDING -> Pair(
            stringResource(id = R.string.bottom_bar_trending),
            Icons.Filled.Star
        )
        SETTINGS -> Pair(
            stringResource(id = R.string.bottom_bar_trending),
            Icons.Filled.Settings
        )
    }

@Composable
private fun NavigationItem(navigationItemVm: NavigationItemViewModel) {
    val colors = MaterialTheme.colors
    val (label, icon) = toImageVector(navigationItemVm.page)

    BottomNavigationItem(
        label = {
            Text(text = label)
        },
        icon = {
            Icon(icon)
        },
        selected = navigationItemVm.isSelected,
        selectedContentColor = colors.secondary,
        unselectedContentColor = colors.onPrimary.copy(
            alpha = ContentAlpha.medium
        ),
        onClick = {
            navigationItemVm.select()
        }
    )
}

@Composable
fun BottomBar(mainVm: MainViewModel) {
    Column {
        Divider(modifier = Modifier.fillMaxWidth())
        BottomNavigation(elevation = 1.dp) {
            mainVm.navigationItems
                .forEach<Pages, NavigationItemViewModel> { entry ->
                    NavigationItem(entry.value)
                }
        }
    }
}

@Composable
fun Settings(vm: MainViewModel) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            Row {
                Text(text = "Dark theme", modifier = Modifier.weight(1f))
                Switch(
                    checked = vm.isDarkTheme,
                    onCheckedChange = {
                        when {
                            vm.isDarkTheme -> vm.darkThemeOff()
                            else -> vm.darkThemeOn()
                        }
                    }
                )
            }
        }
    }
}

private fun repoViewModelMock(): RepoViewModel {
    val description = "It does a lot of cool stuff, try it!! It does a lot " +
        "of cool stuff, try it!! It does a lot of cool stuff, try it!! It " +
        "does a lot of cool stuff, try it!!"

    return RepoViewModel(

        "name-example",
        description,
        "Author",
        1500,
        "https://repo-image-link"
    )
}

@Composable
fun Repos(innerPadding: PaddingValues, mainViewModel: MainViewModel) {
    Surface {
        mainViewModel.loadRepos()
        LazyColumn(contentPadding = innerPadding) {
            items(mainViewModel.repos) { repoViewModel ->
                RepoItem(repoViewModel)
                Divider()
            }
        }
    }
}

@Composable
fun Screen(mainViewModel: MainViewModel) {
    Scaffold(
        topBar = {
            TopBar(mainViewModel)
        },
        bottomBar = {
            BottomBar(mainViewModel)
        }
    ) { innerPadding: PaddingValues ->
        when (mainViewModel.currentlySelectedPage) {
            TRENDING -> Repos(innerPadding, mainViewModel)
            SETTINGS -> Settings(mainViewModel)
        }
    }
}

/**
 *
 *
 * Previews
 *
 *
 **/

@Composable
@Preview(showBackground = true, name = "Repo Item")
fun RepoItemPreview() {
    MyTheme {
        val vm = repoViewModelMock()

        RepoItem(vm)
    }
}

@Composable
@Preview(showBackground = true, name = "Repo Item - Dark theme")
fun RepoItemDarkPreview() {
    MyTheme(isDarkTheme = true) {
        val vm = repoViewModelMock()
        RepoItem(vm)
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsPreview() {
    MyTheme {
        Settings(MainViewModel(ReposGatewayMock()))
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsDarkPreview() {
    MyTheme(isDarkTheme = true) {
        Settings(MainViewModel(ReposGatewayMock()))
    }
}

@Composable
@Preview(showBackground = true)
fun TrendyReposPreview() {
    MyTheme {
        Screen(MainViewModel(ReposGatewayMock()))
    }
}

@Composable
@Preview(showBackground = true, name = "Trendy Repos - Dark theme")
fun TrendyReposDarkPreview() {
    MyTheme(isDarkTheme = true) {
        val mainViewModel = MainViewModel(ReposGatewayMock())
        mainViewModel.darkThemeOn()
        Screen(mainViewModel)
    }
}
