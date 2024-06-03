package me.dizzykitty3.androidtoolkitty.ui.view.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import me.dizzykitty3.androidtoolkitty.R
import me.dizzykitty3.androidtoolkitty.data.sharedpreferences.SettingsSharedPref
import me.dizzykitty3.androidtoolkitty.ui.component.GroupTitle
import me.dizzykitty3.androidtoolkitty.utils.ClipboardUtil
import me.dizzykitty3.androidtoolkitty.utils.HttpUtil
import me.dizzykitty3.androidtoolkitty.utils.SnackbarUtil
import me.dizzykitty3.androidtoolkitty.utils.ToastUtil
import org.json.JSONObject

@Composable
fun UserSyncSection() {
    GroupTitle(R.string.user_sync)

    var dialogState by remember { mutableStateOf<DialogState?>(null) }
    var token by remember { mutableStateOf(SettingsSharedPref.getToken()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val success = stringResource(id = R.string.success)

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedButton(
            onClick = {
                if (token.isBlank()) {
                    dialogState = DialogState.Login
                } else {
                    dialogState = DialogState.UserProfile
                }
            },
            enabled = !isLoading
        ) {
            Text(text = stringResource(id = R.string.user_profile)) // 用户头像按钮
        }

        OutlinedButton(
            onClick = {
                if (token.isBlank()) {
                    dialogState = DialogState.Login
                } else {
                    coroutineScope.launch {
                        isLoading = true
                        handleUploadSettings(
                            token = token,
                            settings = SettingsSharedPref.exportSettingsToJson(),
                            onFailure = {
                                SnackbarUtil.snackbar(view, it)
                                isLoading = false
                            },
                            onSuccess = {
                                SnackbarUtil.snackbar(view, success)
                                isLoading = false
                            }
                        )
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text(text = stringResource(id = R.string.upload_settings))
        }

        OutlinedButton(
            onClick = {
                if (token.isBlank()) {
                    dialogState = DialogState.Login
                } else {
                    coroutineScope.launch {
                        isLoading = true
                        handleDownloadSettings(
                            token = token,
                            onSettingsReceived = {
                                SnackbarUtil.snackbar(view, success)
                                SettingsSharedPref.importSettingsFromJson(it)
                            },
                            onFailure = {
                                SnackbarUtil.snackbar(view, it)
                                isLoading = false
                            },
                            onSuccess = {
                                isLoading = false
                            }
                        )
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text(text = stringResource(id = R.string.download_settings))
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
    }

    when (dialogState) {
        DialogState.Login -> {
            UserLoginDialog(
                onDismiss = { dialogState = null },
                onRegisterClick = { dialogState = DialogState.Register },
                onLoginClick = { usernameForLogin, password ->
                    isLoading = true
                    coroutineScope.launch {
                        handleLogin(
                            usernameForLogin,
                            password,
                            onDismiss = { dialogState = null; isLoading = false },
                            onTokenReceived = { newToken ->
                                token = newToken
                                SettingsSharedPref.setToken(newToken)
                                dialogState = null
                                isLoading = false
                            },
                            onSuccess = {
                                SnackbarUtil.snackbar(view, success)
                            },
                            onFailure = {
                                SnackbarUtil.snackbar(view, it)
                                isLoading = false
                            }
                        )
                    }
                },
                isLoading = isLoading
            )
        }

        DialogState.Register -> {
            UserRegisterDialog(
                onDismiss = { dialogState = null },
                onLoginClick = { dialogState = DialogState.Login },
                onRegisterClick = { username, email, password ->
                    isLoading = true
                    coroutineScope.launch {
                        handleRegister(
                            username,
                            email,
                            password,
                            onDismiss = { dialogState = null; isLoading = false },
                            onTokenReceived = { newToken ->
                                token = newToken
                                SettingsSharedPref.setToken(newToken)
                                dialogState = null
                                isLoading = false
                            },
                            onSuccess = {
                                SnackbarUtil.snackbar(view, success)
                            },
                            onFailure = {
                                SnackbarUtil.snackbar(view, it)
                                isLoading = false
                            }
                        )
                    }
                },
                isLoading = isLoading
            )
        }

        DialogState.UserProfile -> {
            UserProfileDialog(
                token = token,
                onLogout = {
                    SettingsSharedPref.removePreference("token")
                    token = ""
                    dialogState = null
                    isLoading = false
                    SnackbarUtil.snackbar(view, success)
                },
                onDismiss = { dialogState = null }
            )
        }

        null -> {}
    }
}

enum class DialogState {
    Login, Register, UserProfile
}

@Composable
private fun UserLoginDialog(
    onDismiss: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    isLoading: Boolean
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    CommonDialog(onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onRegisterClick)
                )
            }
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = stringResource(id = R.string.username)) })
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                enabled = !isLoading,
                onClick = { onLoginClick(username, password) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }
}

@Composable
private fun UserRegisterDialog(
    onDismiss: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: (String, String, String) -> Unit,
    isLoading: Boolean
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    CommonDialog(onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.login),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onLoginClick)
                )
            }
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = stringResource(id = R.string.username)) })
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) })
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(id = R.string.password)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                enabled = !isLoading,
                onClick = { onRegisterClick(username, email, password) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

@Composable
fun UserProfileDialog(
    token: String,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "User Profile") },
        text = {
            Column {
                Text(
                    text = "Token: $token",
                    modifier = Modifier.clickable {
                        ClipboardUtil.copy(token)
                        ToastUtil.toast("Token copied to clipboard")
                    }
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onLogout() }) {
                    Text("Logout")
                }
                Button(onClick = { onDismiss() }) {
                    Text("Close")
                }
            }
        }
    )
}

@Composable
private fun CommonDialog(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

suspend fun handleUploadSettings(
    token: String,
    settings: String,
    onFailure: (String) -> Unit,
    onSuccess: () -> Unit
) {
    handleRequest(
        url = "https://api.yanqishui.work/toolkitten/data/user-settings",
        headers = mapOf("Authorization" to token),
        body = mapOf("settings" to settings),
        onFailure = onFailure,
        onSuccess = onSuccess,
        requestType = HttpRequestType.PUT
    )
}

suspend fun handleDownloadSettings(
    token: String,
    onSettingsReceived: (String) -> Unit,
    onFailure: (String) -> Unit,
    onSuccess: () -> Unit
) {
    handleRequest(
        url = "https://api.yanqishui.work/toolkitten/data/user-settings",
        headers = mapOf("Authorization" to token),
        onFailure = onFailure,
        onSuccess = onSuccess,
        onDataReceived = onSettingsReceived,
        requestType = HttpRequestType.GET
    )
}

suspend fun handleLogin(
    username: String,
    password: String,
    onDismiss: () -> Unit,
    onTokenReceived: (String) -> Unit,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    handleRequest(
        url = "https://api.yanqishui.work/toolkitten/account/login",
        body = mapOf("username" to username, "password" to password),
        onDismiss = onDismiss,
        onDataReceived = onTokenReceived,
        onSuccess = onSuccess,
        onFailure = onFailure,
        requestType = HttpRequestType.POST
    )
}

suspend fun handleRegister(
    username: String,
    email: String,
    password: String,
    onDismiss: () -> Unit,
    onTokenReceived: (String) -> Unit,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit

) {
    handleRequest(
        url = "https://api.yanqishui.work/toolkitten/account/register",
        body = mapOf("username" to username, "email" to email, "password" to password),
        onDismiss = onDismiss,
        onDataReceived = onTokenReceived,
        onSuccess = onSuccess,
        onFailure = onFailure,
        requestType = HttpRequestType.POST
    )
}

enum class HttpRequestType {
    GET, POST, PUT, DELETE
}

suspend fun handleRequest(
    requestType: HttpRequestType,
    url: String,
    body: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
    onDismiss: () -> Unit = {},
    onDataReceived: (String) -> Unit = {},
    onFailure: (String) -> Unit,
    onSuccess: () -> Unit
) {
    val response: HttpResponse = when (requestType) {
        HttpRequestType.GET -> HttpUtil.get(url, body, headers)
        HttpRequestType.POST -> HttpUtil.post(url, body, headers)
        HttpRequestType.PUT -> HttpUtil.put(url, body, headers)
        HttpRequestType.DELETE -> HttpUtil.delete(url, body, headers)
    }

    if (response.status == HttpStatusCode.OK) {
        val responseBody = response.bodyAsText()
        onDataReceived(responseBody)
        onSuccess()
        onDismiss()
    } else {
        val errorBody = response.bodyAsText()
        val jsonObj = JSONObject(errorBody)
        val errorMessage = jsonObj.getString("message")
        onFailure(errorMessage)
    }
}
