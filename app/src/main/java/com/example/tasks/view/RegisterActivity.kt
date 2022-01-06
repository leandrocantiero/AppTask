package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edit_email
import kotlinx.android.synthetic.main.activity_register.edit_password

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        setListeners();
        setObservers()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {
            handleRegister()
        }
    }

    private fun setListeners() {
        button_save.setOnClickListener(this)
    }

    private fun setObservers() {
        mViewModel.register.observe(this, Observer {
            if (it.success()) {
                Toast.makeText(this, R.string.usuario_criado_sucesso, Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleRegister() {
        if (validateRegister()) {
            val name = edit_name.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()

            mViewModel.register(name, email, password)
        } else {
            Toast.makeText(this, R.string.preencha_todos_campos, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateRegister(): Boolean {
        return edit_name.text.toString() != "" && edit_email.text.toString() != "" && edit_password.text.toString() != ""
    }
}
