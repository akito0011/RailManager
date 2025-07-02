package com.example.trainmanager.activities

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.persistence.percorsi.PercorsiRepository
import com.example.trainmanager.persistence.user.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val itinerariRepository: ItinerariRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _email: MutableLiveData<String> = MutableLiveData()
    val email: LiveData<String>
        get() = _email

    private val _password: MutableLiveData<String> = MutableLiveData()
    val password: LiveData<String>
        get() = _password

    private val _nome: MutableLiveData<String> = MutableLiveData()
    val nome: LiveData<String>
        get() = _nome

    private val _cognome: MutableLiveData<String> = MutableLiveData()
    val cognome: LiveData<String>
        get() = _cognome

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun validateCredentails(): Boolean {
        return _email.value!!.isNotEmpty() && _password.value!!.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
            _email.value!!
        ).matches()
    }

    fun login(): LiveData<Boolean> = liveData {
        if (!validateCredentails()) {
            emit(false)
        } else {
            val response = userRepository.login(_email.value!!, _password.value!!)
            if (response.isSuccessful) {
                _cognome.value = response.body()?.cognome
                _nome.value = response.body()?.nome
                sharedPreferences.edit().apply() {
                    putString("email", email.value)
                    putString("password", password.value)
                    putString("nome", nome.value)
                    putString("cognome", cognome.value)
                    apply()
                }
                syncLocalCache()
            }
            emit(response.isSuccessful)
        }
    }

    fun checkIfUserIsLoggedIn(): Boolean {
        return sharedPreferences.contains("email") && sharedPreferences.contains("password")
    }

    fun syncLocalCache() = viewModelScope.launch {
        itinerariRepository.syncLocalCache()
    }

    companion object {
        fun factory(userRepository: UserRepository, itinerariRepository : ItinerariRepository,  sharedPreferences: SharedPreferences) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return LoginViewModel(userRepository, itinerariRepository, sharedPreferences) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}