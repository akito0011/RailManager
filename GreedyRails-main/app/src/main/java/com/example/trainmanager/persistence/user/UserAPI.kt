package com.example.trainmanager.persistence.user

import com.example.trainmanager.activities.CreateUserDataClass
import com.example.trainmanager.activities.LoginRequestDataClass
import com.example.trainmanager.fragments.percorso.DeleteUserRequestDataClass
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import com.example.trainmanager.fragments.percorso.PercorsoRichiestaDataClass
import com.example.trainmanager.fragments.percorso.StazioneDataClass
import com.example.trainmanager.fragments.trova.RichiestaPercorsiStazioneDataClass
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {

    @POST("$USER_URI/login")
    suspend fun login(@Body loginRequest: LoginRequestDataClass): Response<UserLoginResponseDataClass>

    @POST("$USER_URI/aggiungiutente")
    suspend fun aggiungiUtente(@Body createUserRequest : CreateUserDataClass): Response<JsonElement>

    @POST("$USER_URI/eliminautente")
    suspend fun eliminaUtente(@Body deleteUserRequest : DeleteUserRequestDataClass): Response<JsonElement>

    companion object {
        const val BASE_URL = "http://pwm.matteomiceli.it:7654/"
        const val USER_URI = "pwm"
    }

}