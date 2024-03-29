package me.dizzykitty3.androidtoolkitty.view.card

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomCard
import me.dizzykitty3.androidtoolkitty.common.util.IntentUtils
import me.dizzykitty3.androidtoolkitty.common.util.StringUtils.debugLog

@Composable
fun GoogleMapsCard() {
    val c = LocalContext.current

    CustomCard(
        icon = Icons.Outlined.Place,
        title = c.getString(R.string.google_maps)
    ) {
        var latitude by remember { mutableStateOf("") }
        var longitude by remember { mutableStateOf("") }

        Row {
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text(c.getString(R.string.latitude)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = dimensionResource(id = R.dimen.padding_spacer)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onClickOpenGoogleMapsButton(c, latitude, longitude) }
                )
            )

            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text(c.getString(R.string.longitude)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.padding_spacer)),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onClickOpenGoogleMapsButton(c, latitude, longitude) }
                )
            )
        }

        TextButton(
            onClick = { onClickOpenGoogleMapsButton(c, latitude, longitude) }
        ) {
            Text(text = c.getString(R.string.open_google_maps))

            Icon(
                imageVector = Icons.Outlined.ArrowOutward,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

private fun onClickOpenGoogleMapsButton(c: Context, latitude: String, longitude: String) {
    if (latitude.isBlank() || longitude.isBlank()) return

    IntentUtils(c).openGoogleMaps(latitude, longitude)
    debugLog("onClickOpenGoogleMapsButton")
}