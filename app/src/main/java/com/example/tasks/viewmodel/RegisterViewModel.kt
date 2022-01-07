package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)

    private val mRegister = MutableLiveData<ValidationListener>()
    var register: LiveData<ValidationListener> = mRegister

    fun register(name: String, email: String, password: String) {
        mPersonRepository.register(name, email, password, object : ApiListener {
            override fun onSuccess(model: HeaderModel) {
                mRegister.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mRegister.value = ValidationListener(message)
            }
        })
    }

}