package com.example.trainmanager.persistence

import android.util.Log
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ItinerariRepository(
    private val itinerariLocalDataSource: ItinerariLocalDataSource,
    private val itinerariRemoteDataSource: ItinerariRemoteDataSource
) {

    val allItinerari: Flow<List<ItinerarioDataClass>> = itinerariLocalDataSource.getItinerari()

    suspend fun getPercorsiItinerario(nomeItinerario: String): List<PercorsoDataClass> {
        val percorsiJson = itinerariLocalDataSource.getPercorsiItinerario(nomeItinerario)
        return Gson().fromJson(percorsiJson, Array<PercorsoDataClass>::class.java)?.toList()
            ?: emptyList()
    }

    suspend fun insertItinerario(
        nomeItinerario: String,
        dataInizio: String,
        notifiche: Int
    ): Boolean =
        itinerariRemoteDataSource.creaItinerario(
            nomeItinerario,
            dataInizio,
            notifiche
        ).let { apiResult ->
            if (apiResult.isSuccessful) {
                itinerariLocalDataSource.insertItinerario(
                    ItinerarioEntity(
                        nomeItinerario,
                        dataInizio,
                        0,
                        notifiche,
                        ""
                    )
                )
            } else {
                Log.e("ItinerariAPIError", apiResult.message())
            }
            return@insertItinerario apiResult.isSuccessful
        }

    suspend fun deleteItinerario(nomeItinerario: String): Boolean =
        itinerariRemoteDataSource.eliminaitinerario(nomeItinerario).let { apiResult ->
            if (apiResult.isSuccessful) {
                itinerariLocalDataSource.deleteItinerario(nomeItinerario)
            } else {
                Log.e("ItinerariAPIError", apiResult.message())
            }
            return@deleteItinerario apiResult.isSuccessful
        }

    suspend fun aggiungiPercorsoItinerario(
        nomeItinerario: String,
        percorso: PercorsoDataClass,
        tolleranza: Int,
        senza_cambi: Boolean,
        diretto: Boolean
    ): Boolean {
        itinerariRemoteDataSource.aggiungiPercorsoItinerario(
            nomeItinerario,
            percorso,
            tolleranza,
            senza_cambi,
            diretto
        ).let { apiResult ->
            if (apiResult.isSuccessful) {
                val percorsiJson =
                    itinerariLocalDataSource.getPercorsiItinerario(
                        nomeItinerario
                    )
                val percorsi = Gson().fromJson(
                    percorsiJson,
                    Array<PercorsoDataClass>::class.java
                )?.toMutableList() ?: mutableListOf()
                percorsi.add(percorso)
                val percorsiJsonNuovo = Gson().toJson(percorsi)
                itinerariLocalDataSource.updatePercorsoItinerario(
                    nomeItinerario,
                    percorsiJsonNuovo
                )
            } else {
                Log.e("ItinerariAPIError", apiResult.message())
            }
            return@aggiungiPercorsoItinerario apiResult.isSuccessful
        }
    }

    suspend fun eliminaUltimoPercorsoItinerario(nomeItinerario: String): Boolean {
        itinerariRemoteDataSource.eliminaUltimoPercorsoItinerario(nomeItinerario).let { apiResult ->
            if (apiResult.isSuccessful) {
                val percorsiJson = itinerariLocalDataSource.getPercorsiItinerario(nomeItinerario)
                val percorsi = Gson().fromJson(percorsiJson, Array<PercorsoDataClass>::class.java)
                    ?.toMutableList() ?: mutableListOf()
                percorsi.removeLast()
                val percorsiJsonNuovo = Gson().toJson(percorsi)
                itinerariLocalDataSource.updatePercorsoItinerario(nomeItinerario, percorsiJsonNuovo)
            } else {
                Log.e("ItinerariAPIError", apiResult.message())
            }
            return@eliminaUltimoPercorsoItinerario apiResult.isSuccessful
        }
    }

    suspend fun updatePercorsoItinerario(nomeItinerario: String, percorsoJson: String) =
        itinerariLocalDataSource.updatePercorsoItinerario(
            nomeItinerario,
            percorsoJson
        )

    suspend fun deleteAllItinerari() = itinerariLocalDataSource.deleteAllItinerari()

    suspend fun syncLocalCache() {
        itinerariRemoteDataSource.getItinerari().let { apiResult ->
            if (apiResult.isSuccessful) {
                Log.d("KEK", apiResult.body().toString())
                itinerariLocalDataSource.deleteAllItinerari()
                apiResult.body()?.forEach {
                    itinerariRemoteDataSource.ottieniPercorsiItinerario(
                        it.nome_itinerario
                    ).let { apiResultPercorsi ->
                        if (apiResultPercorsi.isSuccessful) {
                            val percorsiJson = Gson().toJson(apiResultPercorsi.body())
                            val itinerarioEntity =
                                ItinerarioEntity(
                                    it.nome_itinerario,
                                    it.data_inizio,
                                    it.numero_percorsi,
                                    it.notifiche,
                                    percorsiJson
                                )
                            itinerariLocalDataSource.insertItinerario(
                                itinerarioEntity
                            )
                        } else {
                            Log.e(
                                "ItinerariAPIError",
                                apiResultPercorsi.message()
                            )
                        }
                    }
                }
            } else {
                Log.e("ItinerariAPIError", apiResult.message())
            }
        }
    }
}