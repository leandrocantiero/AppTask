package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners();
        setObservers()

        verifyLoggedUser()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }

    private fun setObservers() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.loggedUser.observe(this, Observer {
            if (it) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
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
