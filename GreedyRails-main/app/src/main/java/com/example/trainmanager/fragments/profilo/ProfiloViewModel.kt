package com.example.trainmanager.fragments.profilo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trainmanager.fragments.home.HomeViewModel
import com.example.trainmanager.fragments.percorso.DeleteUserRequestDataClass
import com.example.trainmanager.persistence.ItinerariAPI
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.persistence.user.UserAPI
import com.example.trainmanager.persistence.user.UserRepository
import kotlinx.coroutines.launch

class ProfiloViewModel(private val userRepository : UserRepository, private val itinerariRepository: ItinerariRepository, private val sharedPreferences: SharedPreferences) : ViewModel() {

    val nome : String = sharedPreferences.getString("nome", "").toString()
    val cognome : String = sharedPreferences.getString("cognome", "").toString()
    val email : String = sharedPreferences.getString("email", "").toString()
    private val password : String = sharedPreferences.getString("password", "").toString()

    fun eliminaAccount() : LiveData<Boolean> = liveData {
        val result = userRepository.eliminaUtente(email, password)
        if (result) {
            sharedPreferences.edit().clear().apply()
            itinerariRepository.deleteAllItinerari()
        }
        emit(result)
    }

    companion object {
        fun factory(userRepository : UserRepository, ItinerariRepository : ItinerariRepository, sharedPreferences: SharedPreferences) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProfiloViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProfiloViewModel(userRepository, ItinerariRepository, sharedPreferences) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}