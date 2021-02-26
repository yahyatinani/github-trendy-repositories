package com.why.githubtrendyrepos.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.why.githubtrendyrepos.app.DataLimitReached
import com.why.githubtrendyrepos.app.GetTrendyReposUseCase
import com.why.githubtrendyrepos.app.NoConnectivity
import com.why.githubtrendyrepos.app.RateLimitExceeded
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.githubtrendyrepos.viewmodels.MainViewModel
import com.why.githubtrendyrepos.viewmodels.NavigationItemViewModel
import com.why.githubtrendyrepos.viewmodels.Pages
import com.why.githubtrendyrepos.viewmodels.Pages.SETTINGS
import com.why.githubtrendyrepos.viewmodels.Pages.TRENDING
import com.why.githubtrendyrepos.viewmodels.RepoViewModel
import com.why.githubtrendyrepos.viewmodels.ReposGatewayMock
import com.why.template.compose.R
import com.why.template.compose.R.string.err_api_rate_limit_exceeded
import com.why.template.compose.R.string.err_no_internet_connection
import com.why.template.compose.R.string.err_no_more_data_available

@Composable
fun NetworkImage(
    url: String?,
    modifier: Modifier,
    placeholder: @Composable () -> Unit
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var isImageReady by remember { mutableStateOf(false) }

    DisposableEffect(url) {
        var picasso: Picasso? = null

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
        try {
            picasso = Picasso.get()

            picasso
                .load(url)
                .resize(24, 24)
                .centerCrop()
                .into(target)
        } catch (e: Exception) {
            Log.println(Log.ERROR, "PICASSO", e.toString())
        }

        onDispose {
            image = null
            isImageReady = false
            picasso?.cancelRequest(target)
        }
    }

    when {
        isImageReady -> Image(
            bitmap = image!!,
            contentDescription = null,
            modifier = modifier,
        )
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
                            contentDescription = null,
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
                        contentDescription = null,
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
fun BottomBar(mainVm: MainViewModel) {
    Column {
        Divider(modifier = Modifier.fillMaxWidth())
        BottomNavigation(elevation = 1.dp) {
            mainVm.navigationItems
                .forEach<Pages, NavigationItemViewModel> { entry ->
                    val colors = MaterialTheme.colors
                    val navigationItemVm = entry.value
                    val (label, icon) = toImageVector(navigationItemVm.page)

                    BottomNavigationItem(
                        label = {
                            Text(text = label)
                        },
                        icon = {
                            Icon(icon, contentDescription = null)
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
fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun NoMoreDataMessage(message: String) {
    Surface(
        color = MaterialTheme.colors.error
    ) {
        val boldStyle = TextStyle(fontWeight = FontWeight.Bold)
        Text(
            text = message,
            style = MaterialTheme.typography.caption.merge(boldStyle),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.surface,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun errorMessage(loadState: LoadState) =
    when ((loadState as LoadState.Error).error) {
        is RateLimitExceeded -> stringResource(err_api_rate_limit_exceeded)
        is DataLimitReached -> stringResource(err_no_more_data_available)
        is NoConnectivity -> stringResource(err_no_internet_connection)
        else -> "Unknown error!"
    }

@Composable
fun Repos(innerPadding: PaddingValues, mainViewModel: MainViewModel) {
    Surface {
        val repos = mainViewModel.reposPagination().collectAsLazyPagingItems()
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxHeight()
        ) {
            items(repos) { vm ->
                if (vm != null) {
                    RepoItem(vm)
                    Divider()
                }
            }
            repos.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item() {
                        ProgressIndicator()
                    }
                    loadState.append is LoadState.Loading -> item {
                        ProgressIndicator()
                    }
                    loadState.refresh is LoadState.Error -> item {
                        NoMoreDataMessage(errorMessage(loadState.refresh))
                    }
                    loadState.append is LoadState.Error -> item {
                        NoMoreDataMessage(errorMessage(loadState.append))
                    }
                }
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
@Preview(showBackground = true, name = "No data error")
fun NoDataMessagePreview() {
    MyTheme {
        NoMoreDataMessage("No more data available")
    }
}

@Composable
@Preview(showBackground = true, name = "No data error - Dark")
fun NoDataMessageDarkPreview() {
    MyTheme(isDarkTheme = true) {
        NoMoreDataMessage("No more data available")
    }
}

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
        RepoItem(repoViewModelMock())
    }
}

private fun mainViewModel() =
    MainViewModel(GetTrendyReposUseCase(ReposGatewayMock()))

@Composable
@Preview(showBackground = true)
fun SettingsPreview() {
    MyTheme {
        Settings(mainViewModel())
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsDarkPreview() {
    MyTheme(isDarkTheme = true) {
        Settings(mainViewModel())
    }
}

@Composable
@Preview(showBackground = true)
fun TrendyReposPreview() {
    MyTheme {
        Screen(mainViewModel())
    }
}

@Composable
@Preview(showBackground = true, name = "Trendy Repos - Dark theme")
fun TrendyReposDarkPreview() {
    MyTheme(isDarkTheme = true) {
        val mainViewModel = mainViewModel()
        mainViewModel.darkThemeOn()
        Screen(mainViewModel)
    }
}
