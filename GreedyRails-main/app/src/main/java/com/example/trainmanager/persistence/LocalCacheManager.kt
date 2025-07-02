package com.example.trainmanager.persistence

import android.content.Context
import androidx.room.Room
import com.example.trainmanager.fragments.dettagliIitinerario.OttieniPercorsiDataClass
import com.example.trainmanager.fragments.itinerario.OttieniItinerariDataClass
import com.example.trainmanager.fragments.percorso.ApiInterface
import com.example.trainmanager.fragments.percorso.RetrofitInstance
import com.google.gson.Gson

object LocalCacheManager {
    private lateinit var apiInterface: ApiInterface
    lateinit var db: AppDatabase
    lateinit var email : String
    lateinit var password : String

    fun init(context: Context, email : String, password : String) {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "room-db"
        ).build()
        LocalCacheManager.email = email
        LocalCacheManager.password = password
    }

    suspend fun refreshCache() {
        val callItinerari = apiInterface.ottieniItinerari(OttieniItinerariDataClass(email, password))
        val response = callItinerari.execute()
        if (response.isSuccessful) {
            //db.userDao().deleteAllItinerari()
            //val itinerari = response.body()
            //itinerari?.forEach {
               // val callPercorsi = apiInterface.ottieniPercorsiItinerario(OttieniPercorsiDataClass(
                  //  email, password, it.nome_itinerario))
                //val percorsi = callPercorsi.execute().body()
                //val gson = Gson()
                //val jsonPercorsi = gson.toJson(percorsi)
                //val itinerarioEntity = ItinerarioEntity(it.nome_itinerario, it.data_inizio, it.numero_percorsi, (it.notifiche == 1), jsonPercorsi)
                //db.userDao().insertItinerario(itinerarioEntity)
            }
        }
    }