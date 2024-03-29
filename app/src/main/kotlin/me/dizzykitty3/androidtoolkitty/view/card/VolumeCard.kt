package me.dizzykitty3.androidtoolkitty.view.card

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomCard
import me.dizzykitty3.androidtoolkitty.common.ui.component.CustomSpacerPadding
import me.dizzykitty3.androidtoolkitty.common.util.AudioUtils
import me.dizzykitty3.androidtoolkitty.common.util.ToastUtils
import me.dizzykitty3.androidtoolkitty.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeCard() {
    val c = LocalContext.current
    CustomCard(
        icon = Icons.AutoMirrored.Outlined.VolumeUp,
        title = c.getString(R.string.volume)
    ) {
        val maxVolume = AudioUtils(c).getMaxVolumeIndex()

        val customVolume = SettingsViewModel().getCustomVolume(c)
        var mCustomVolume by remember { mutableIntStateOf(customVolume) }

        val customVolumeOptionLabel = SettingsViewModel().getCustomVolumeOptionLabel(c)
        var mCustomVolumeOptionLabel by remember { mutableStateOf(customVolumeOptionLabel) }

        val options = listOf(
            c.getString(R.string.mute),
            "40%",
            "60%",
            if (mCustomVolumeOptionLabel != "") mCustomVolumeOptionLabel
            else {
                if (mCustomVolume == -1) "+"
                else "${mCustomVolume}%"
            }
        )

        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        var showVolumeDialog by remember { mutableStateOf(false) }

        var showVolumeOptionLabelDialog by remember { mutableStateOf(false) }

        Text(text = c.getString(R.string.media_volume))

        CustomSpacerPadding()

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
            space = SegmentedButtonDefaults.BorderWidth
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    onClick = {
                        selectedIndex = index
                        when (index) {
                            0 -> {
                                AudioUtils(c).setVolume(0)
                            }

                            1 -> {
                                AudioUtils(c).setVolume((0.4 * maxVolume).toInt())
                            }

                            2 -> {
                                AudioUtils(c).setVolume((0.6 * maxVolume).toInt())
                            }

                            3 -> {
                                if (mCustomVolume != -1) {
                                    AudioUtils(c).setVolume((mCustomVolume * 0.01 * maxVolume).toInt())
                                } else
                                    showVolumeDialog = true
                            }
                        }
                    },
                    selected = index == selectedIndex,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    )
                ) {
                    Text(text = label.toString())
                }
            }

            if (showVolumeDialog) {
                var newCustomVolume by remember { mutableFloatStateOf(0f) }

                AlertDialog(
                    onDismissRequest = {
                        showVolumeDialog = false
                    },
                    title = {
                        Text(text = "${c.getString(R.string.add_custom_volume)}\n${newCustomVolume.toInt()}% -> ${(newCustomVolume * 0.01 * maxVolume).toInt()}/$maxVolume")
                    },
                    text = {
                        Slider(
                            value = newCustomVolume,
                            onValueChange = {
                                newCustomVolume = it
                            },
                            valueRange = 0f..100f
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if ((newCustomVolume * 0.01 * maxVolume).toInt() == 0 && newCustomVolume.toInt() != 0) {
                                    ToastUtils(c).showToast(c.getString(R.string.system_media_volume_levels_limited))
                                    return@Button
                                } else {
                                    SettingsViewModel().setCustomVolume(c, newCustomVolume.toInt())
                                    mCustomVolume = newCustomVolume.toInt()
                                    showVolumeOptionLabelDialog = true
                                    AudioUtils(c).setVolume((mCustomVolume * 0.01 * maxVolume).toInt())
                                }
                            }
                        ) {
                            Text(text = c.getString(android.R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showVolumeDialog = false }
                        ) {
                            Text(text = c.getString(android.R.string.cancel))
                        }
                    },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_dialog))
                )
            }

            if (showVolumeOptionLabelDialog) {
                var optionLabel by remember { mutableStateOf("") }

                AlertDialog(
                    onDismissRequest = {
                        showVolumeOptionLabelDialog = false
                    },
                    title = {
                        Text(text = c.getString(R.string.you_can_set_a_label_for_it))
                    },
                    text = {
                        OutlinedTextField(
                            value = optionLabel,
                            onValueChange = { optionLabel = it },
                            label = { Text(text = c.getString(R.string.label)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    SettingsViewModel().setCustomVolumeOptionLabel(
                                        c,
                                        optionLabel
                                    )
                                }
                            )
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                SettingsViewModel().setCustomVolumeOptionLabel(c, optionLabel)
                                mCustomVolumeOptionLabel = optionLabel
                                showVolumeOptionLabelDialog = false
                                showVolumeDialog = false
                            }
                        ) {
                            Text(text = c.getString(android.R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showVolumeOptionLabelDialog = false
                                showVolumeDialog = false
                            }
                        ) {
                            Text(text = c.getString(android.R.string.cancel))
                        }
                    },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_dialog))
                )
            }
        }

        Button(
            onClick = { showVolumeDialog = true }
        ) {
            Text(text = c.getString(R.string.reset))
        }
    }
}