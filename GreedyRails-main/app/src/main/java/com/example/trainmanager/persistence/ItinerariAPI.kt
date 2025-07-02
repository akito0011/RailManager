package com.example.trainmanager.persistence

import com.example.trainmanager.fragments.dettagliIitinerario.CreaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.EliminaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.percorso.AggiungiPercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ItinerariAPI {
    @POST("$ITINERARI_URI/creaitinerario")
    suspend fun creaItinerario(@Body itinerarioDataClass: CreaItinerarioDataClass) : Response<JsonElement>

    @POST("$ITINERARI_URI/aggiungipercorsoitinerario")
    suspend fun aggiungiPercorsoItinerario(@Body aggiungiPercorsoDataClass: AggiungiPercorsoDataClass) : Response<JsonObject>

    @POST("$ITINERARI_URI/ottienipercorsiitinerario")
    suspend fun ottieniPercorsiItinerario(@Body itinerarioDataClass: OttieniPercorsiDataClass) : Response<List<PercorsoDataClass>>

    @POST("$ITINERARI_URI/ottieniitinerari")
    suspend fun ottieniItinerari(@Body ottieniItinerariDataClass: OttieniItinerariDataClass) : Response<List<ItinerarioDataClass>>

    @POST("$ITINERARI_URI/eliminaitinerario")
    suspend fun eliminaItinerario(@Body itinerarioDataClass: EliminaItinerarioDataClass) : Response<JsonElement>

    @POST("$ITINERARI_URI/eliminaultimopercorsoitinerario")
    suspend fun eliminaUltimoPercorsoItinerario(@Body eliminaUltimoPercorsoRequest: EliminaUltimoPercorsoRequestDataClass) : Response<JsonElement>

    companion object {
        const val BASE_URL = "http://pwm.matteomiceli.it:7654/"
        const val ITINERARI_URI = "pwm"

    }
}