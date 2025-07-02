package com.example.trainmanager.fragments.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trainmanager.persistence.ItinerariRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val itinerariRepository : ItinerariRepository, private val sharedPreferences : SharedPreferences) : ViewModel() {

    val nome : String = sharedPreferences.getString("nome", "").toString()

    fun logout() : LiveData<Boolean> = liveData {
        itinerariRepository.deleteAllItinerari()
        sharedPreferences.edit().clear().apply()
        emit(true)
    }

    companion object {
        fun factory(repository: ItinerariRepository, sharedPreferences: SharedPreferences) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(repository, sharedPreferences) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}