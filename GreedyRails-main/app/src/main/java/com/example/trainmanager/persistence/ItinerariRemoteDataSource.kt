package com.example.trainmanager.persistence

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.work.impl.model.Preference
import com.example.trainmanager.fragments.dettagliIitinerario.CreaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.EliminaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.percorso.AggiungiPercorsoDataClass
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItinerariRemoteDataSource(private val itinerariAPI : ItinerariAPI, private val sharedPreference: SharedPreferences) {

    private val email = sharedPreference.getString("email", "").toString()
    private val password = sharedPreference.getString("password", "").toString()

    suspend  fun getItinerari() = itinerariAPI.ottieniItinerari(OttieniItinerariDataClass(email, password))

    suspend fun creaItinerario(nomeItinerario : String, dataInizio : String, notifiche : Int) = itinerariAPI.creaItinerario(CreaItinerarioDataClass(email, password, nomeItinerario, dataInizio, notifiche))

    suspend fun eliminaitinerario(nomeItinerario : String) = itinerariAPI.eliminaItinerario(EliminaItinerarioDataClass(email, password, nomeItinerario))

    suspend fun eliminaUltimoPercorsoItinerario(nomeItinerario: String) = itinerariAPI.eliminaUltimoPercorsoItinerario(EliminaUltimoPercorsoRequestDataClass(email, password, nomeItinerario))

    suspend fun ottieniPercorsiItinerario(nomeItinerario : String) = itinerariAPI.ottieniPercorsiItinerario(OttieniPercorsiDataClass(email, password, nomeItinerario))

    suspend fun aggiungiPercorsoItinerario(nomeItinerario : String, percorso: PercorsoDataClass, tolleranza: Int, senza_cambi : Boolean, diretto : Boolean) : Response<JsonObject> {
        val stazione_partenza_provincia = percorso.tappe[0].stazione_provincia
        val stazione_partenza_citta = percorso.tappe[0].stazione_citta
        val stazione_partenza_nome_stazione = percorso.tappe[0].stazione_nome
        val stazione_destinazione_provincia = percorso.tappe[percorso.tappe.size - 1].stazione_provincia
        val stazione_destinazione_citta = percorso.tappe[percorso.tappe.size - 1].stazione_citta
        val stazione_destinazione_nome_stazione = percorso.tappe[percorso.tappe.size - 1].stazione_nome
        val orario_partenza = percorso.tappe[0].orario_arrivo
        val aggiungiPercorsoDataClass = AggiungiPercorsoDataClass(email, password, nomeItinerario, stazione_partenza_provincia, stazione_partenza_citta, stazione_partenza_nome_stazione, stazione_destinazione_provincia, stazione_destinazione_citta, stazione_destinazione_nome_stazione, orario_partenza, tolleranza, senza_cambi, diretto)
        return itinerariAPI.aggiungiPercorsoItinerario(aggiungiPercorsoDataClass)
    }
}