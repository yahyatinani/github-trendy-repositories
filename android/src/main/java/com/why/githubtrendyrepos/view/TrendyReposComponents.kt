package com.why.githubtrendyrepos.view

import androidx.compose.foundation.Image
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.why.githubtrendyrepos.theme.MyTheme
import com.why.template.compose.R

@Composable
fun RepoItem() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val type = MaterialTheme.typography

        Text(text = "example-api-name", style = type.subtitle1)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "It does a lot of cool stuff, try it!! It does a lot of " +
                "cool stuff, try it!! It does a lot of cool stuff, try it!! " +
                "It does a lot of cool stuff, try it!!",
            maxLines = 2,
            style = type.caption,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .weight(weight = 1f)
            ) {
                Image(
                    imageVector = vectorResource(R.drawable.img_placeholder),
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )
                //TODO: Put the repos image in here.
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Author",
                    style = type.overline,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Row(modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.StarOutline,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "1.5k",
                    style = type.overline,
                    modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RepoItemPreview() {
    MyTheme {
        RepoItem()
    }
}

@Composable
@Preview
fun TrendyReposPreview() {
    MyTheme {}
}
