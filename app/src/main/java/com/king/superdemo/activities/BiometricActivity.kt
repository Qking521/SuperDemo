package com.king.superdemo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.king.superdemo.R
import com.king.superdemo.extension.toast

class BiometricActivity : BaseActivity() {

    lateinit var biometricManager: BiometricManager
    lateinit var biometricPrompt: BiometricPrompt

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
        biometricManager = getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun fingerprint(view: View) {
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricDialog()
        } else {
            showBiometricError()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun face(view: View) {
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricDialog()
        } else {
            showBiometricError()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showBiometricDialog() {

        //1 创建识别提示框
        biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("biometric")
                .setDescription("request biometric")
                .setNegativeButton(
                        "cancel",
                        mainExecutor,
                        DialogInterface.OnClickListener { dialog, which -> Log.i("wq", "biometric: "); }
                )
                .build()

        //2 创建识别取消的回调
        val  cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener { Log.i("wq", "biometric cancel"); }

        //3 创建认证回调接口实现
        val  authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                Log.i("wq", "onAuthenticationError: ");
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                Log.i("wq", "onAuthenticationSucceeded: ");
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
                Log.i("wq", "onAuthenticationHelp: ");
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.i("wq", "onAuthenticationFailed: ");
            }
        }

        //4 开始认证
        biometricPrompt.authenticate(cancellationSignal, mainExecutor, authenticationCallback)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun showBiometricError() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {toast("hardware unavailable")}
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {toast("no biometric enrolled")}
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {toast("no biometric hardware")}
        }
    }
}