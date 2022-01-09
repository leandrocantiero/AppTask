package com.example.tasks.service.helper

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.tasks.view.LoginActivity
import java.util.concurrent.Executor

class FingerPrintHelper(val context: Context) {

    fun isAuthenticationAvaliable(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return false
        }

        val biometricManager: BiometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false
        }

        return false
    }
}