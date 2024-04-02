package me.dizzykitty3.androidtoolkitty.view.card

import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomCard
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomGroupDivider
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomGroupTitleText
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomSystemSettingsButton
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomTip
import me.dizzykitty3.androidtoolkitty.viewmodel.SettingsViewModel

private const val SETTING_1 = "setting_display"
private const val SETTING_2 = "setting_auto_rotate"
private const val SETTING_3 = "setting_bluetooth"
private const val SETTING_4 = "setting_default_apps"
private const val SETTING_5 = "setting_battery_optimization"
private const val SETTING_6 = "setting_caption"
private const val SETTING_7 = "setting_locale"
private const val SETTING_8 = "setting_date_and_time"
private const val SETTING_9 = "setting_developer"

@Composable
fun SystemSettingsCard() {
    CustomCard(
        icon = Icons.Outlined.Settings,
        title = R.string.android_system_settings
    ) {
        val context = LocalContext.current
        val settingsViewModel = remember { SettingsViewModel() }

        val settings = listOf(
            Setting(SETTING_1, R.string.open_display_settings),
            Setting(SETTING_2, R.string.open_auto_rotate_settings),
            Setting(SETTING_3, R.string.open_bluetooth_settings),
            Setting(SETTING_4, R.string.open_default_apps_settings),
            Setting(SETTING_5, R.string.open_battery_optimization_settings),
            Setting(SETTING_6, R.string.open_caption_preferences),
            Setting(SETTING_7, R.string.open_language_settings),
            Setting(SETTING_8, R.string.open_date_and_time_settings),
            Setting(SETTING_9, R.string.open_developer_options)
        )

        val isShowSetting = remember {
            mutableStateMapOf<String, Boolean>().apply {
                settings.forEach { setting ->
                    this[setting.settingType] =
                        settingsViewModel.getCardShowedState(context, setting.settingType)
                }
            }
        }

        val isShowGroupTitle1 = settings.subList(0, 6).any { setting ->
            isShowSetting[setting.settingType] == true
        }

        val isShowGroupTitle2 = settings.subList(6, settings.size).any { setting ->
            isShowSetting[setting.settingType] == true
        }

        if (!checkIsAutoTime(context)) CustomTip(resId = R.string.set_time_automatically_is_off_tip)

        if (isShowGroupTitle1) CustomGroupTitleText(R.string.common)

        settings.subList(0, 6).forEach { setting ->
            if (isShowSetting[setting.settingType] == true) {
                CustomSystemSettingsButton(
                    settingType = setting.settingType,
                    buttonText = setting.buttonText
                )
            }
        }

        if (isShowGroupTitle1 && isShowGroupTitle2) CustomGroupDivider()

        if (isShowGroupTitle2) CustomGroupTitleText(R.string.debugging)

        settings.subList(6, settings.size).forEach { setting ->
            if (isShowSetting[setting.settingType] == true) {
                CustomSystemSettingsButton(
                    settingType = setting.settingType,
                    buttonText = setting.buttonText
                )
            }
        }
    }
}

private fun checkIsAutoTime(context: Context): Boolean {
    val contentResolver: ContentResolver = context.contentResolver
    val isAutoTime = Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME, 0)
    return isAutoTime == 1
}

data class Setting(val settingType: String, val buttonText: Int)