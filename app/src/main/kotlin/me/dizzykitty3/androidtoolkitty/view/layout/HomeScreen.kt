package me.dizzykitty3.androidtoolkitty.view.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.foundation.ui_component.CustomCardNoIcon
import me.dizzykitty3.androidtoolkitty.foundation.ui_component.CustomCardSpacePadding
import me.dizzykitty3.androidtoolkitty.foundation.ui_component.CustomOneHandedModePadding
import me.dizzykitty3.androidtoolkitty.foundation.ui_component.CustomScreen
import me.dizzykitty3.androidtoolkitty.foundation.ui_component.CustomTip
import me.dizzykitty3.androidtoolkitty.view.card.AndroidVersionsCard
import me.dizzykitty3.androidtoolkitty.view.card.CheckAppOnAppMarketCard
import me.dizzykitty3.androidtoolkitty.view.card.ClipboardCard
import me.dizzykitty3.androidtoolkitty.view.card.GoogleMapsCard
import me.dizzykitty3.androidtoolkitty.view.card.GreetingText
import me.dizzykitty3.androidtoolkitty.view.card.SocialMediaProfileCard
import me.dizzykitty3.androidtoolkitty.view.card.SystemSettingsCard
import me.dizzykitty3.androidtoolkitty.view.card.UnicodeCard
import me.dizzykitty3.androidtoolkitty.view.card.UrlCard
import me.dizzykitty3.androidtoolkitty.view.card.VolumeCard
import me.dizzykitty3.androidtoolkitty.view.card.YearProgressCard
import me.dizzykitty3.androidtoolkitty.viewmodel.SettingsViewModel
import java.util.Locale

private const val CARD_1 = "card_year_progress"
private const val CARD_2 = "card_volume"
private const val CARD_3 = "card_clipboard"
private const val CARD_4 = "card_url"
private const val CARD_5 = "card_social_media_profile"
private const val CARD_6 = "card_android_system_settings"
private const val CARD_7 = "card_unicode"
private const val CARD_8 = "card_google_maps"
private const val CARD_9 = "card_open_app_on_google_play"
private const val CARD_10 = "card_android_versions"

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val settingsViewModel = remember { SettingsViewModel() }

    CustomScreen {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                GreetingText()
            }

            IconButton(
                onClick = {
                    navController.navigate("SettingsScreen")
                    settingsViewModel.setHaveOpenedSettingsScreen(context, true)
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        CustomCardSpacePadding()

        if (settingsViewModel.getIsOneHandedMode(context)) CustomOneHandedModePadding()

        val locale = Locale.getDefault().toString()
        if (!(locale.contains(Regex("en|Hans|zh_CN|zh_SG")))) CustomTip(
            formattedMessage = stringResource(
                R.string.no_translation,
                locale
            )
        )

        val cardMapping = mapOf(
            CARD_1 to settingsViewModel.getCardShowedState(context, CARD_1),
            CARD_2 to settingsViewModel.getCardShowedState(context, CARD_2),
            CARD_3 to settingsViewModel.getCardShowedState(context, CARD_3),
            CARD_4 to settingsViewModel.getCardShowedState(context, CARD_4),
            CARD_5 to settingsViewModel.getCardShowedState(context, CARD_5),
            CARD_6 to settingsViewModel.getCardShowedState(context, CARD_6),
            CARD_7 to settingsViewModel.getCardShowedState(context, CARD_7),
            CARD_8 to settingsViewModel.getCardShowedState(context, CARD_8),
            CARD_9 to settingsViewModel.getCardShowedState(context, CARD_9),
            CARD_10 to settingsViewModel.getCardShowedState(context, CARD_10),
        )

        cardMapping.forEach { (cardName, isShow) ->
            if (isShow) {
                when (cardName) {
                    CARD_1 -> YearProgressCard()
                    CARD_2 -> VolumeCard()
                    CARD_3 -> ClipboardCard()
                    CARD_4 -> UrlCard()
                    CARD_5 -> SocialMediaProfileCard()
                    CARD_6 -> SystemSettingsCard()
                    CARD_7 -> UnicodeCard()
                    CARD_8 -> GoogleMapsCard()
                    CARD_9 -> CheckAppOnAppMarketCard()
                    CARD_10 -> AndroidVersionsCard()
                }
            }
        }

        CustomCardNoIcon(title = R.string.test) {
            Button(
                onClick = { navController.navigate("LuckySpinningWheelScreen") }
            ) {
                Text(text = stringResource(R.string.lucky_spinning_wheel))
            }
            Button(
                onClick = { navController.navigate("BluetoothDevicesScreen") }
            ) {
                Text(text = stringResource(id = R.string.bluetooth_devices))
            }
        }
    }
}