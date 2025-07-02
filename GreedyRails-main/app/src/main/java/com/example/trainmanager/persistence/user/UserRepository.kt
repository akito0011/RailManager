package com.example.trainmanager.persistence.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.trainmanager.activities.CreateUserDataClass
import com.example.trainmanager.activities.LoginRequestDataClass
import com.example.trainmanager.fragments.percorso.DeleteUserRequestDataClass
import retrofit2.Response

class UserRepository(private val userRemoteDataSource: UserRemoteDataSource) {

    suspend fun login(email: String, password: String): Response<UserLoginResponseDataClass> =
        userRemoteDataSource.login(LoginRequestDataClass(email, password))

    suspend fun aggiungiUtente(
        email: String,
        nome: String,
        cognome: String,
        password: String
    ): Boolean = userRemoteDataSource.aggiungiUtente(
        CreateUserDataClass(
            email,
            nome,
            cognome,
            password
        )
    ).isSuccessful

    suspend fun eliminaUtente(email: String, password: String): Boolean = userRemoteDataSource.eliminaUtente(
        DeleteUserRequestDataClass(
            email,
            password
        )
    ).isSuccessful

}