package me.dizzykitty3.androidtoolkitty.foundation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.dizzykitty3.androidtoolkitty.data.sharedpreferences.SettingsSharedPref

@Composable
fun CustomHideCardSettingSwitch(
    resId: Int,
    cardId: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val settingsSharedPref = remember { SettingsSharedPref }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onCheckedChange(!isChecked)
            settingsSharedPref.saveCardShowedState(cardId, !isChecked)
        }
    ) {
        Text(text = stringResource(id = resId))
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}