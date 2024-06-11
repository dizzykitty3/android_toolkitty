package me.dizzykitty3.androidtoolkitty.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import me.dizzykitty3.androidtoolkitty.R

@Composable
fun CustomCard(
    icon: ImageVector? = null,
    @StringRes titleRes: Int,
    content: @Composable () -> Unit
) {
    CustomCard(
        icon = icon,
        title = stringResource(id = titleRes),
        content = content
    )
}

@Composable
fun CustomCard(
    icon: ImageVector? = null,
    title: String,
    content: @Composable () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        val cardPadding = Modifier.padding(dimensionResource(id = R.dimen.padding_card_content))

        Column(modifier = cardPadding) {
            if (icon == null) {
                CardTitle(title = title)
                TitleDivider()
                CardContent { content() }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    CardTitle(title = title)
                }
                TitleDivider()
                CardContent { content() }
            }
        }
    }
    CardSpacePadding()
}

@Composable
private fun CardTitle(title: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = title,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun CardContent(content: @Composable () -> Unit) {
    Column {
        SpacerPadding()
        SpacerPadding()
        Column { content() }
    }
}