package com.example.trainmanager.fragments.itinerario

import androidx.room.ColumnInfo
import androidx.room.Entity

data class ItinerarioDataClass(
    val nome_itinerario: String,
    val data_inizio: String,
    val numero_percorsi : Int,
    val notifiche : Int
)
