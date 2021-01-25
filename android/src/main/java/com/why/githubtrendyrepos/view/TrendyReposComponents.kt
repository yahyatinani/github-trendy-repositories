package com.why.githubtrendyrepos.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.why.githubtrendyrepos.theme.MyTheme

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
    Surface {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            val typography = MaterialTheme.typography

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
@Preview
fun TrendyReposPreview() {
    MyTheme {}
}
