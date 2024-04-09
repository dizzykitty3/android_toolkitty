package me.dizzykitty3.androidtoolkitty.foundation.utils

import android.util.Log
import android.widget.Toast
import me.dizzykitty3.androidtoolkitty.ToolKittyApp.Companion.app

object TToast {
    private const val TAG = "ToastService"

    @JvmStatic
    fun toast(message: String) = Toast.makeText(app, message, Toast.LENGTH_SHORT).show()

    @JvmStatic
    fun toastAndLog(logEvent: String) {
        Log.d(TAG, logEvent)
        toast(logEvent)
    }
}