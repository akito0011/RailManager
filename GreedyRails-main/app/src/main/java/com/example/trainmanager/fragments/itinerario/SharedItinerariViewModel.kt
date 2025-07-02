package com.example.trainmanager.fragments.itinerario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.utils.forceRefresh
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SharedItinerariViewModel(private val repository: ItinerariRepository) : ViewModel() {

    val allItinerari: LiveData<List<ItinerarioDataClass>> = repository.allItinerari.asLiveData()

    private val _selectedItinerario: MutableLiveData<ItinerarioDataClass> = MutableLiveData()
    val selectedItinerario: LiveData<ItinerarioDataClass>
        get() = _selectedItinerario

    private val _percorsiSelectedItinerario: MutableLiveData<MutableList<PercorsoDataClass>> =
        MutableLiveData(mutableListOf())
    val percorsiSelectedItinerario: LiveData<MutableList<PercorsoDataClass>>
        get() = _percorsiSelectedItinerario

    fun selectItinerario(selectedItinerario: ItinerarioDataClass) = viewModelScope.launch {
        _selectedItinerario.value = selectedItinerario
                //repository.getPercorsiItinerario(selectedItinerario.nome_itinerario).collect() {
            //_percorsiSelectedItinerario.value = it.toMutableList()
        //}
        _percorsiSelectedItinerario.value = repository.getPercorsiItinerario(selectedItinerario.nome_itinerario).toMutableList()
    }

    fun insertItinerario(itinerario: ItinerarioDataClass): LiveData<Boolean> = liveData {
        _selectedItinerario.value = itinerario
        emit(
            repository.insertItinerario(
                selectedItinerario.value?.nome_itinerario!!,
                selectedItinerario.value?.data_inizio!!,
                selectedItinerario.value?.notifiche!!
            )
        )
        _percorsiSelectedItinerario.value = mutableListOf()
    }

    fun deleteItinerario(): LiveData<Boolean> =
        liveData { emit(repository.deleteItinerario(selectedItinerario.value?.nome_itinerario!!)) }

    fun aggiungiPercorsoItinerario(
        percorso: PercorsoDataClass,
        tolleranza: Int,
        senza_cambi: Boolean,
        diretto: Boolean
    ): LiveData<Boolean> = liveData {
        _percorsiSelectedItinerario.value?.add(percorso)
        emit(
        repository.aggiungiPercorsoItinerario(
            selectedItinerario.value?.nome_itinerario!!,
            percorso,
            tolleranza,
            senza_cambi,
            diretto
        )
        )
    }

    fun eliminaUltimoPercorsoItinerario(): LiveData<Boolean> = liveData {
        _percorsiSelectedItinerario.value?.removeLast()
        emit(repository.eliminaUltimoPercorsoItinerario(selectedItinerario.value?.nome_itinerario!!))
    }

    companion object {
        fun factory(repository: ItinerariRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SharedItinerariViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SharedItinerariViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}