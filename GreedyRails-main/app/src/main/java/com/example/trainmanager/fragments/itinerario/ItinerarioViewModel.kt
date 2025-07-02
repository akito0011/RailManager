package com.example.trainmanager.fragments.itinerario

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.trainmanager.MyApplication
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.persistence.ItinerarioEntity
import kotlinx.coroutines.launch

class ItinerarioViewModel(private val repository : ItinerariRepository) : ViewModel() {

    val allItinerari : LiveData<List<ItinerarioDataClass>> = repository.allItinerari.asLiveData()

    companion object {
        fun factory(repository: ItinerariRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ItinerarioViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ItinerarioViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}

