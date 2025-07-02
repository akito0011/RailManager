package com.example.trainmanager.activities

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.trainmanager.persistence.user.UserRepository

class RegistrazioneViewModel(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _nome: MutableLiveData<String> = MutableLiveData()
    val nome: MutableLiveData<String>
        get() = _nome

    private val _cognome: MutableLiveData<String> = MutableLiveData()
    val cognome: MutableLiveData<String>
        get() = _cognome

    private val _email: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String>
        get() = _email

    private val _password: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String>
        get() = _password

    private fun validateCredentials(): Boolean {
        return _nome.value!!.isNotEmpty() && _cognome.value!!.isNotEmpty() && _email.value!!.isNotEmpty() && _password.value!!.isNotEmpty() && _password.value!!.length >= 8 && android.util.Patterns.EMAIL_ADDRESS.matcher(
            _email.value!!
        ).matches()
    }

    fun setNome(nome: String) {
        _nome.value = nome
    }

    fun setCognome(cognome: String) {
        _cognome.value = cognome
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun registraUtente(): LiveData<Boolean> = liveData {
        if (!validateCredentials()) {
            emit(false)
        } else {
            val result = userRepository.aggiungiUtente(
                _email.value!!,
                _nome.value!!,
                _cognome.value!!,
                _password.value!!
            )
            if (result) {
                sharedPreferences.edit().apply() {
                    putString("email", _email.value)
                    putString("password", _password.value)
                    putString("nome", _nome.value)
                    putString("cognome", _cognome.value)
                    apply()
                }
            }
            emit(result)
        }
    }

    companion object {
        fun factory(repository: UserRepository, sharedPreferences: SharedPreferences) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RegistrazioneViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return RegistrazioneViewModel(repository, sharedPreferences) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
