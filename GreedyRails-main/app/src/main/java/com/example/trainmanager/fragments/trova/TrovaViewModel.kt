package com.example.trainmanager.fragments.trova

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoRichiestaDataClass
import com.example.trainmanager.fragments.percorso.PercorsoViewModel
import com.example.trainmanager.fragments.percorso.StazioneDataClass
import com.example.trainmanager.persistence.percorsi.PercorsiRepository

class TrovaViewModel(private val percorsiRepository: PercorsiRepository) : ViewModel() {

    private val _selectedStazionePartenza: MutableLiveData<StazioneDataClass> = MutableLiveData()
    val selectedStazionePartenza: LiveData<StazioneDataClass>
        get() = _selectedStazionePartenza

    private val _suggerimenti: MutableLiveData<List<StazioneDataClass>> = MutableLiveData()
    val suggerimenti: LiveData<List<StazioneDataClass>>
        get() = _suggerimenti

    private val _percorsi: MutableLiveData<List<PercorsoDataClass>> = MutableLiveData()
    val percorsi: LiveData<List<PercorsoDataClass>>
        get() = _percorsi

    fun selectStazionePartenza(
        provinciaStazione: String,
        cittaStazione: String,
        nomeStazione: String
    ) : LiveData<Boolean> = liveData {
        _selectedStazionePartenza.value =
            StazioneDataClass(provinciaStazione, cittaStazione, nomeStazione)
        val response = percorsiRepository.getPercorsiStazione(
            provinciaStazione,
            cittaStazione,
            nomeStazione
        )
        _percorsi.value = response.body()
        emit(response.isSuccessful)
    }

    fun setSuggerimenti(regex: String): LiveData<Boolean> = liveData {
        val response = percorsiRepository.getStazioni(regex)
        _suggerimenti.value = response.body() ?: emptyList()
        emit(response.isSuccessful)
    }

    fun setPercorsi(tolleranza: Int, senzaCambi: Boolean, diretto: Boolean): LiveData<Boolean> =
        liveData {
            val response = percorsiRepository.getPercorsiStazione(
                _selectedStazionePartenza.value!!.provincia,
                _selectedStazionePartenza.value!!.citta,
                _selectedStazionePartenza.value!!.nome_stazione,
            )
            _percorsi.value = response.body()
            emit(response.isSuccessful)
        }

    companion object {
        fun factory(repository: PercorsiRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TrovaViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TrovaViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}