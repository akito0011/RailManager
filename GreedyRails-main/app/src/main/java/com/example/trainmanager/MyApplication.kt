package com.example.trainmanager

import android.app.Application
import com.example.trainmanager.persistence.AppDatabase
import com.example.trainmanager.persistence.ItinerariAPI
import com.example.trainmanager.persistence.ItinerariAPI.Companion.BASE_URL
import com.example.trainmanager.persistence.ItinerariLocalDataSource
import com.example.trainmanager.persistence.ItinerariRemoteDataSource
import com.example.trainmanager.persistence.ItinerariRepository
import com.example.trainmanager.persistence.percorsi.PercorsiAPI
import com.example.trainmanager.persistence.percorsi.PercorsiRemoteDataSource
import com.example.trainmanager.persistence.percorsi.PercorsiRepository
import com.example.trainmanager.persistence.user.UserAPI
import com.example.trainmanager.persistence.user.UserRemoteDataSource
import com.example.trainmanager.persistence.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val dataBase by lazy { AppDatabase.getDatabase(this) }
    val itinerariAPIClient : ItinerariAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItinerariAPI::class.java)
    }
    val percorsiAPIClient : PercorsiAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PercorsiAPI::class.java)
    }
    val userAPIClient : UserAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }
    val sharedPreferences by lazy { getSharedPreferences("UserSettings", MODE_PRIVATE) }
    private val itinerariLocalDataSource by lazy { ItinerariLocalDataSource(dataBase.itinerariDao()) }
    private val itinerariRemoteDataSource by lazy { ItinerariRemoteDataSource(itinerariAPIClient, sharedPreferences) }
    private val percorsiRemoteDataSource by lazy { PercorsiRemoteDataSource(percorsiAPIClient) }
    private val userRemoteDataSource by lazy { UserRemoteDataSource(userAPIClient) }
    val itinerariRepository by lazy { ItinerariRepository(itinerariLocalDataSource, itinerariRemoteDataSource) }
    val percorsiRepository by lazy { PercorsiRepository(percorsiRemoteDataSource) }
    val userRepository by lazy { UserRepository(userRemoteDataSource) }
}