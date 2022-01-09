package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.helper.FingerPrintHelper
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
        setObservers()

        mViewModel.isAuthenticationAvaliable()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_login -> {
                handleLogin()
            }
            R.id.text_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    private fun showAuthentication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this@LoginActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação por digital")
            .setSubtitle("Faça o login utilizando sua digital")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)
    }

    private fun setObservers() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.fingerPrint.observe(this, Observer {
            if (it) {
                showAuthentication()
            }
        })
    }

    private fun handleLogin() {
        if (validateLogin()) {
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()

            mViewModel.login(email, password)
        } else {
            Toast.makeText(this, R.string.preencha_todos_campos, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateLogin(): Boolean {
        return edit_email.text.toString() != "" && edit_password.text.toString() != ""
    }
}
