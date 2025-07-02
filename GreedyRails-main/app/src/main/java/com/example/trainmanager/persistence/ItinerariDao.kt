package com.example.trainmanager.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trainmanager.fragments.itinerario.ItinerarioDataClass
import com.example.trainmanager.fragments.percorso.Percorso
import com.example.trainmanager.fragments.percorso.PercorsoDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface ItinerariDao {
    @Query("DELETE FROM Itinerario")
    suspend fun deleteAllItinerari() : Int

    @Insert
    suspend fun insertItinerario(itinerario: ItinerarioEntity)

    @Query("SELECT * FROM Itinerario")
    fun getItinerari() : Flow<List<ItinerarioDataClass>>

    //@Query("SELECT percorsi FROM Itinerario WHERE nome_itinerario = :nome_itinerario")
    //fun getPercorsiItinerario(nome_itinerario: String): Flow<String>

    @Query("SELECT percorsi FROM Itinerario WHERE nome_itinerario = :nome_itinerario")
    suspend fun getPercorsiItinerario(nome_itinerario: String): String

    @Query("UPDATE Itinerario SET percorsi = :percorsi WHERE nome_itinerario = :nome_itinerario")
    suspend fun updatePercorsoItinerario(nome_itinerario: String, percorsi: String) : Int

    @Query("DELETE FROM Itinerario WHERE nome_itinerario = :nome_itinerario")
    suspend fun deleteItinerario(nome_itinerario: String) : Int
}