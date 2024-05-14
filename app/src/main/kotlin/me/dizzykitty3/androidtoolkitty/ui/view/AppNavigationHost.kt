package me.dizzykitty3.androidtoolkitty.ui.view

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.dizzykitty3.androidtoolkitty.foundation.const.EDIT_HOME_SCREEN
import me.dizzykitty3.androidtoolkitty.foundation.const.HOME_SCREEN
import me.dizzykitty3.androidtoolkitty.foundation.const.PERMISSION_REQUEST_SCREEN
import me.dizzykitty3.androidtoolkitty.foundation.const.QR_CODE_GENERATOR_SCREEN
import me.dizzykitty3.androidtoolkitty.foundation.const.SETTINGS_SCREEN
import me.dizzykitty3.androidtoolkitty.ui.view.edit_home_screen.EditHomeScreen
import me.dizzykitty3.androidtoolkitty.ui.view.home_screen.HomeScreen
import me.dizzykitty3.androidtoolkitty.ui.view.permission_request_screen.PermissionRequestScreen
import me.dizzykitty3.androidtoolkitty.ui.view.qr_code_generator_screen.QrCodeGeneratorScreen
import me.dizzykitty3.androidtoolkitty.ui.view.settings_screen.SettingsScreen

@Composable
fun AppNavigationHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 100)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 100)) }
    ) {
        composable(HOME_SCREEN) { HomeScreen(navController) }
        composable(SETTINGS_SCREEN) { SettingsScreen(navController) }
        composable(EDIT_HOME_SCREEN) { EditHomeScreen() }
        composable(PERMISSION_REQUEST_SCREEN) { PermissionRequestScreen() }
        composable(QR_CODE_GENERATOR_SCREEN) { QrCodeGeneratorScreen() }
    }
}