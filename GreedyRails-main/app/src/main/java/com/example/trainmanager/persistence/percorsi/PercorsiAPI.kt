package com.example.trainmanager.persistence.percorsi

import com.example.trainmanager.fragments.dettagliIitinerario.CreaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.EliminaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.percorso.AggiungiPercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoRichiestaDataClass
import com.example.trainmanager.fragments.percorso.StazioneDataClass
import com.example.trainmanager.fragments.trova.RichiestaPercorsiStazioneDataClass
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PercorsiAPI {

    @POST("$PERCORSI_URI/percorso")
    suspend fun getPercorso(@Body percorsoRichiesta : PercorsoRichiestaDataClass) : Response<List<PercorsoDataClass>>

    @GET("$PERCORSI_URI/stazione/{nomeStazione}")
    suspend fun getStazioni(@Path("nomeStazione") nomeStazione : String) : Response<List<StazioneDataClass>>

    @POST("$PERCORSI_URI/percorsistazione")
    suspend fun percorsiStazione(@Body stazioneDataClass: RichiestaPercorsiStazioneDataClass) : Response<List<PercorsoDataClass>>

    companion object {
        const val BASE_URL = "http://pwm.matteomiceli.it:7654/"
        const val PERCORSI_URI = "pwm"
    }

}