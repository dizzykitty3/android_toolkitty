package me.dizzykitty3.androidtoolkitty.common.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import me.dizzykitty3.androidtoolkitty.R

@Composable
fun CustomAlertDialogButton(
    buttonText: String,
    dialogMessageTitle: String,
    dialogMessage: @Composable () -> Unit,
    positiveButtonText: String?,
    negativeButtonText: String?,
    onClickAction: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        ),
        onClick = { showDialog = true }
    ) {
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.onError
        )
    }

    if (showDialog) {
        val c = LocalContext.current

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = dialogMessageTitle) },
            text = { dialogMessage() },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onClickAction()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = positiveButtonText ?: c.getString(android.R.string.ok),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(text = negativeButtonText ?: c.getString(android.R.string.cancel))
                }
            },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_dialog))
        )
    }
}