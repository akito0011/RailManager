package com.example.trainmanager.fragments.percorso

import com.example.trainmanager.activities.CreateUserDataClass
import com.example.trainmanager.activities.LoginRequestDataClass
import com.example.trainmanager.activities.UserInfoRequestDataClass
import com.example.trainmanager.activities.UserInfoResultDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.CreaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.EliminaItinerarioDataClass
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.trova.RichiestaPercorsiStazioneDataClass
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @POST("login")
    fun login(@Body request : LoginRequestDataClass) : Call<AuthStatusDataClass>
    @POST("userinfo")
    fun getUserInfo(@Body request : UserInfoRequestDataClass) : Call<UserInfoResultDataClass>
    @POST("percorso")
    fun getPercorso(@Body percorsoRichiesta : PercorsoRichiestaDataClass) : Call<List<PercorsoDataClass>>
    @GET("stazione/{nomeStazione}")
    fun getStazioni(@Path("nomeStazione") nomeStazione : String) : Call<List<StazioneDataClass>>
    @POST("aggiungipercorsoitinerario")
    fun aggiungiPercorsoItinerario(@Body aggiungiPercorsoDataClass: AggiungiPercorsoDataClass) : Call<AggiungiPercorsoDataClass>
    @POST("creaitinerario")
    fun creaItinerario(@Body itinerarioDataClass: CreaItinerarioDataClass) : Call<CreaItinerarioDataClass>
    @POST("ottienipercorsiitinerario")
    fun ottieniPercorsiItinerario(@Body itinerarioDataClass: OttieniPercorsiDataClass) : Call<List<PercorsoDataClass>>
    @POST("ottieniitinerari")
    fun ottieniItinerari(@Body ottieniItinerariDataClass: OttieniItinerariDataClass) : Call<List<ItinerarioDataClass>>
    @POST("aggiungiutente")
    fun aggiungiUtente(@Body aggiungiUtenteDataClass: CreateUserDataClass) : Call<CreateUserDataClass>
    @POST("eliminautente")
    fun eliminaUtente(@Body eliminaUtenteDataClass: DeleteUserRequestDataClass) : Call<DeleteUserRequestDataClass>
    @POST("eliminaitinerario")
    fun eliminaItinerario(@Body itinerarioDataClass: EliminaItinerarioDataClass) : Call<EliminaItinerarioDataClass>
    @POST("eliminaultimopercorsoitinerario")
    fun eliminaUltimoPercorsoItinerario(@Body itinerarioDataClass: EliminaItinerarioDataClass) : Call<EliminaItinerarioDataClass>
    @POST("percorsistazione")
    fun percorsiStazione(@Body stazioneDataClass: RichiestaPercorsiStazioneDataClass) : Call<List<PercorsoDataClass>>
}