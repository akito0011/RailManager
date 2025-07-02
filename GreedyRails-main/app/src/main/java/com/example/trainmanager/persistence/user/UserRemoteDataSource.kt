package com.example.trainmanager.persistence.user

import com.example.trainmanager.activities.CreateUserDataClass
import com.example.trainmanager.activities.LoginRequestDataClass
import com.example.trainmanager.fragments.percorso.DeleteUserRequestDataClass
import com.google.gson.JsonElement
import retrofit2.Response

class UserRemoteDataSource(private val userAPI : UserAPI) {

    suspend fun login(loginRequest : LoginRequestDataClass) : Response<UserLoginResponseDataClass> = userAPI.login(loginRequest)

    suspend fun aggiungiUtente(createUserRequest : CreateUserDataClass) : Response<JsonElement> = userAPI.aggiungiUtente(createUserRequest)

    suspend fun eliminaUtente(deleteUserRequest : DeleteUserRequestDataClass) : Response<JsonElement> = userAPI.eliminaUtente(deleteUserRequest)

}