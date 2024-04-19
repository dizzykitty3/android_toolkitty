package me.dizzykitty3.androidtoolkitty

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import me.dizzykitty3.androidtoolkitty.data.sharedpreferences.SettingsSharedPref
import me.dizzykitty3.androidtoolkitty.foundation.ui.theme.ToolKittyTheme
import me.dizzykitty3.androidtoolkitty.foundation.util.ClipboardUtil
import me.dizzykitty3.androidtoolkitty.ui.MainLayout

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var isAutoClearClipboard = false

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToolKittyTheme(
                dynamicColor = SettingsSharedPref.dynamicColor
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MainLayout()
                }
            }
        }
        Log.d(TAG, "onCreate called")
    }

    override fun onResume() {
        super.onResume()
        getSettingsSharedPreferences()
        Log.d(TAG, "onResume called")
    }

    private fun getSettingsSharedPreferences() {
        isAutoClearClipboard = SettingsSharedPref.autoClearClipboard
        Log.d(TAG, "settings sp got")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus and isAutoClearClipboard) // Clipboard operations require window focus
            ClipboardUtil.clear()
    }

    override fun onStop() {
        super.onStop()
        currentFocus?.clearFocus() // To collapse keyboard
        Log.d(TAG, "focus cleared")
    }
}