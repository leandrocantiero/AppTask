package com.example.tasks.viewmodel

import android.app.Application
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.helper.FingerPrintHelper
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient
import java.util.concurrent.Executor

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mFingerPrint = MutableLiveData<Boolean>()
    var fingerPrint: LiveData<Boolean> = mFingerPrint

    fun login(email: String, password: String) {
        mPersonRepository.login(email, password, object : ApiListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                RetrofitClient.addHeader(model.token, model.personKey)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationListener(message)
            }
        })
    }

    fun isAuthenticationAvaliable() {
        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val logged = (token != "" && personKey != "")

        RetrofitClient.addHeader(token, personKey)

        if (!logged) {
            mPriorityRepository.get()
        }

        if (FingerPrintHelper(getApplication()).isAuthenticationAvaliable()) {
            mFingerPrint.value = logged
        }
    }
}