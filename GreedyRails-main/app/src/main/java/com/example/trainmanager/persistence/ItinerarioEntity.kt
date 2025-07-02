package com.example.trainmanager.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Itinerario")
data class ItinerarioEntity(
    @PrimaryKey val nome_itinerario: String,
    val data_inizio: String,
    val numero_percorsi : Int,
    val notifiche : Int,
    val percorsi : String
)
