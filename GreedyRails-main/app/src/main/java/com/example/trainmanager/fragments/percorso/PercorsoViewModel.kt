package com.example.trainmanager.fragments.percorso

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.persistence.percorsi.PercorsiRepository
import retrofit2.Response

class PercorsoViewModel(val percorsiRepository: PercorsiRepository) : ViewModel() {

    private val _selectedStazionePartenza: MutableLiveData<StazioneDataClass> = MutableLiveData()
    val selectedStazionePartenza: LiveData<StazioneDataClass>
        get() = _selectedStazionePartenza

    private val _selectedStazioneDestinazione: MutableLiveData<StazioneDataClass> =
        MutableLiveData()
    val selectedStazioneDestinazione: LiveData<StazioneDataClass>
        get() = _selectedStazioneDestinazione

    val _selectedDataPartenza: MutableLiveData<String> = MutableLiveData()
    val selectedDataPartenza: LiveData<String>
        get() = _selectedDataPartenza

    private val _suggerimenti: MutableLiveData<List<StazioneDataClass>> = MutableLiveData(listOf())
    val suggerimenti: LiveData<List<StazioneDataClass>>
        get() = _suggerimenti

    private val _percorsi: MutableLiveData<List<PercorsoDataClass>> = MutableLiveData(listOf())
    val percorsi: LiveData<List<PercorsoDataClass>>
        get() = _percorsi

    fun selectStazionePartenza(
        provinciaStazione: String,
        cittaStazione: String,
        nomeStazione: String
    ) {
        _selectedStazionePartenza.value =
            StazioneDataClass(provinciaStazione, cittaStazione, nomeStazione)
    }

    fun selectStazioneDestinazione(
        provinciaStazione: String,
        cittaStazione: String,
        nomeStazione: String
    ) {
        _selectedStazioneDestinazione.value =
            StazioneDataClass(provinciaStazione, cittaStazione, nomeStazione)
    }

    fun selectDataPartenza(dataPartenza: String) {
        _selectedDataPartenza.value = dataPartenza
    }

    fun setSuggerimenti(regex: String): LiveData<Boolean> = liveData {
        val response = percorsiRepository.getStazioni(regex)
        _suggerimenti.value = response.body() ?: emptyList()
        emit(response.isSuccessful)
    }

    fun setPercorsi(tolleranza: Int, senzaCambi: Boolean, diretto: Boolean): LiveData<Boolean> =
        liveData {
            val response = percorsiRepository.getPercorso(
                _selectedStazionePartenza.value!!.provincia,
                _selectedStazionePartenza.value!!.citta,
                _selectedStazionePartenza.value!!.nome_stazione,
                _selectedStazioneDestinazione.value!!.provincia,
                _selectedStazioneDestinazione.value!!.citta,
                _selectedStazioneDestinazione.value!!.nome_stazione,
                _selectedDataPartenza.value!!,
                tolleranza,
                senzaCambi,
                diretto
            )
            _percorsi.value = response.body()
            emit(response.isSuccessful)
        }

    companion object {
        fun factory(repository: PercorsiRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PercorsoViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PercorsoViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
